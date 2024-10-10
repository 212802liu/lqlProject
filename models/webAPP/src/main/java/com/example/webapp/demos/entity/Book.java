package com.example.webapp.demos.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Book {
    String author;

    String publisher;

    String id;
}
