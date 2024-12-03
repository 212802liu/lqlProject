package com.example.react.service.impl;

import com.example.react.entity.TBook;
import com.example.react.service.BookService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class BookServiceImpl implements BookService {

    private Map<String, TBook> books = new HashMap<>();
    {
        books.put("1",new TBook(1L,"pb",1L));
        books.put("2",new TBook(2L,"pb",2L));
    }

    @Override
    public Mono<TBook> findById(String id) {
        return Mono.justOrEmpty(books.get(id));
    }

    @Override
    public Mono<TBook> save(TBook TBook) {
        long id = new Random().nextLong();
        TBook.setId(id);
        books.put(id+"", TBook);
        return Mono.just(TBook);
    }

    @Override
    public Flux<TBook> findAll() {
        return Flux.fromIterable(books.values());
    }
}
