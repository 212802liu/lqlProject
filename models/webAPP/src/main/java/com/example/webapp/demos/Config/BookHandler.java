package com.example.webapp.demos.Config;

import com.example.webapp.demos.entity.Book;
import com.example.webapp.demos.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
//@Component
public class BookHandler {

//    @Autowired
    private final BookService bookService;

    public BookHandler(BookService bookService) {
        this.bookService = bookService;
    }

    public Mono<ServerResponse> listBooks(ServerRequest request) {
        return ServerResponse.ok().body(bookService.findAll(), Book.class);
    }

    public Mono<ServerResponse> createBook(ServerRequest request) {
        Mono<Book> bookMono = request.bodyToMono(Book.class);
        return bookMono.flatMap(book ->
                ServerResponse.status(HttpStatus.CREATED).body(bookService.save(book), Book.class));
    }}
