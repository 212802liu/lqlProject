package com.example.webapp.demos.Config;

import com.example.webapp.demos.entity.Book;
import com.example.webapp.demos.service.BookService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * todo 这种方式没用！！！ 待解决
 *
 */
@Configuration
public class BookRouter {

    @Bean
    public RouterFunction<ServerResponse> bookRoutes(BookHandler handler) {
        return route(GET("/webFlux"), handler::listBooks)
                .andRoute(POST("/webFlux"), handler::createBook);
    }


}



