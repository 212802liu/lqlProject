package com.example.react;

import com.example.react.entity.TBook;
import com.example.react.entity.TBookAuthor;
import com.example.react.mapper.TBookMapper;
import dev.miku.r2dbc.mysql.MySqlConnectionConfiguration;
import dev.miku.r2dbc.mysql.MySqlConnectionFactory;
import io.r2dbc.spi.Connection;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.Duration;

@SpringBootTest
public class AppTest {

    @Autowired
    TBookMapper bookMapper;

    @Autowired
    DatabaseClient databaseClient;

    @Test
    void testCRUD() throws IOException {
//         bookMapper.findAll().subscribe(System.out::println);
//        bookMapper.findById(1L).subscribe(System.out::println);
        TBookAuthor block = bookMapper.authorBook(1L).block();
        System.out.println(block);
        // 需要阻塞一下
        System.in.read();



    }

    @Test
    void testDatabaseClient(){

        databaseClient.sql("SELECT * FROM t_author")
                .fetch()  // 开始执行查询
                .all()    // 获取所有结果
                .subscribe(result -> {
                    // 处理每一个结果
                    System.out.println(result);
                });
    }

    //思想：
    // 1、有了r2dbc，我们的应用在数据库层面天然支持高并发、高吞吐量。
    // 2、并不能提升开发效率

    @Test
    void connection() throws IOException {

        // r2dbc基于全异步、响应式、消息驱动
        // jdbc:mysql://localhost:3306/test
        // r2dbc:mysql://localhost:3306/test

        //0、MySQL配置
        MySqlConnectionConfiguration configuration = MySqlConnectionConfiguration.builder()
                .host("192.168.32.140")
                .port(3306)
                .username("ajxt")
                .password("Ajxt520!")
                .database("ruoyi-vue-pro")
                .connectTimeout(Duration.ofSeconds(3))
                .build();

        //1、获取连接工厂
        MySqlConnectionFactory connectionFactory = MySqlConnectionFactory.from(configuration);


        //2、获取到连接，发送sql
        Mono<Connection> connectionMono = Mono.from(connectionFactory.create());

        // JDBC： Statement： 封装sql的
        //3、数据发布者
        connectionMono.flatMapMany(connection ->
                                    connection
//                                    .createStatement("INSERT INTO `t_book` (`publisher`, `author`) VALUES ('who', '1')")
                                    .createStatement("select * from t_book where id=?id and publisher=?")
                                    .bind("id", 1L) //具名参数
                                    .bind(1, "pub")
                                    .execute()
        ).flatMap(result -> {

            return result.map((readable,book)->{
                System.out.println("readable:"+readable);
                System.out.println("book:"+book);
                        Long id = readable.get("id", Long.class);
                        String publisher = readable.get("publisher", String.class);
                        Long author = readable.get("author_id", Long.class);
                        return new TBook(author,publisher,id);
            });

        }).subscribe(tAuthor -> System.out.println("book = " + tAuthor));


        //背压； 不用返回所有东西，基于请求量返回；

        System.in.read();


    }

}
