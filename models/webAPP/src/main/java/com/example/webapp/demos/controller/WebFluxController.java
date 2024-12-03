package com.example.webapp.demos.controller;

import com.example.webapp.demos.entity.Book;
import com.example.webapp.demos.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/webFlux")
public class WebFluxController {
    @Autowired
    private BookService bookService;


//    @GetMapping("/{name}")
//    public Mono<String> name(@PathVariable String name) {
//        return Mono.just(name);
//    }

    @GetMapping("/age/{name}")
    public int age(@PathVariable int name) {
        return (name);
    }


    @GetMapping
    public Flux<Book> list() {
        return bookService.findAll();
    }

    @GetMapping("/{id}")
    public Mono<Book> getById(@PathVariable String id) {
        return bookService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Book> create(@RequestBody Book book) {
        return bookService.save(book);
    }




}

