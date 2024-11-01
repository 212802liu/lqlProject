package com.example.webFlux.Controller;

import com.example.webFlux.entity.Book;
import com.example.webFlux.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
@RequestMapping("/webFlux")
@Tag(name = "WebFluxDemoController", description = "响应式相关接口")
public class WebFluxDemoController {

    @Autowired
    private BookService bookService;

    @GetMapping
    @Operation(summary = "获取书籍列表", description = "获取所有书籍")
    public Flux<Book> list() {
        return bookService.findAll();
    }


    @Operation(summary = "管理员添加用户",
            description = "根据姓名添加用户并返回",
            parameters = {
                    @Parameter(name = "name", description = "姓名")
            },
            responses = {
                    @ApiResponse(description = "返回添加的用户", content = @Content(mediaType = "application/json", schema = @Schema(anyOf = {String.class, Book.class}))),
                    @ApiResponse(responseCode = "400", description = "返回400时候错误的原因")
            }
    )
    @PostMapping("/addBook")
    Mono<Book> addUser(@RequestBody Book newBook){
        return bookService.save(newBook);
    }
}
