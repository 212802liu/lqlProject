package com.example.webapp.demos.service.impl;

import com.example.webapp.demos.entity.Book;
import com.example.webapp.demos.service.BookService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
@Service
public class BookServiceImpl implements BookService {

    private Map<String, Book> books = new HashMap<>();
    {
        books.put("1",new Book("liu","pb","1"));
    }

    @Override
    public Mono<Book> findById(String id) {
        return Mono.justOrEmpty(books.get(id));
    }

    @Override
    public Mono<Book> save(Book book) {
        String id = UUID.randomUUID().toString();
        book.setId(id);
        books.put(id, book);
        return Mono.just(book);

    }

    @Override
    public Flux<Book> findAll() {
        return Flux.fromIterable(books.values());
    }
}
