package com.example.react.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "书籍信息")
@Table("t_book")
public class TBook {
    @Schema(description = "作者")
    Long authorId;

    @Schema(description = "出版社")
    String publisher;

    @Schema(description = "主键")
    @Id
    Long id;

//    public static void main(String[] args) {
//        List<Book> books = new ArrayList<>();
//        books.add(new Book("liu","pb",1L));
//        books.add(new Book("zhang","pb",2L));
//        books.add(new Book("wu","pb2",3L));
//
//        books.stream()
//                .filter(t->t.getPublisher().equals("pb"))
//                .peek(t-> System.out.println("peek is like log。book peek："+t.toString()))
//                .map((t)->{
//                    System.out.println("map: "+t.getAuthor());
//                    return t.getAuthor();
//                })
//                .sorted(String::compareTo)
//                .peek(t-> System.out.println("author peek："+t.toString()))
//                .forEach(u->{
//                    System.out.println("foreach "+u);
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        throw new RuntimeException(e);
//                    }
//                });
//    }
}
