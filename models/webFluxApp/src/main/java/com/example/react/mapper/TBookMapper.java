package com.example.react.mapper;

import com.example.react.entity.TBook;
import com.example.react.entity.TBookAuthor;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface TBookMapper extends R2dbcRepository<TBook,Long> {

    // 1-1关联关系； 查出这本图书以及它的作者
    @Query("select b.*,t.name as name from t_book b" +
            " LEFT JOIN t_author t on b.author_id = t.id " +
            " WHERE b.id = :bookId")
    Mono<TBookAuthor> authorBook(@Param("bookId") Long bookId);


//    @Query("SELECT * FROM person WHERE lastname = :lastname")
//    Flux<Person> findByLastname(String lastname);
//
//    @Query("SELECT firstname, lastname FROM person WHERE lastname = $1")
//    Mono<Person> findFirstByLastname(String lastname);

}
