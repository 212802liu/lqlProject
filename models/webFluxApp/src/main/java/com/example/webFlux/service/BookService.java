package com.example.webFlux.service;

import com.example.webFlux.entity.Book;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BookService {
    Mono<Book> findById(String id);

    Mono<Book> save(Book book);

    Flux<Book> findAll();
}
