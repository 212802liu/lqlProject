package com.example.react.service;

import com.example.react.entity.TBook;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BookService {
    Mono<TBook> findById(String id);

    Mono<TBook> save(TBook TBook);

    Flux<TBook> findAll();
}
