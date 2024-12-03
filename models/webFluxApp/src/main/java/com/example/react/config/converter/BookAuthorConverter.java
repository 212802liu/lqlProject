package com.example.react.config.converter;

import com.example.react.entity.TAuthor;
import com.example.react.entity.TBookAuthor;
import io.r2dbc.spi.Row;
import org.springframework.core.convert.converter.Converter;

import org.springframework.data.convert.ReadingConverter;

import java.time.Instant;

/**
 * 告诉Spring Data 怎么封装 TBookAuthor 对象
 */
@ReadingConverter
public class BookAuthorConverter implements Converter<Row, TBookAuthor> {
    //1）、@Query 指定了 sql如何发送
    //2）、自定义 BookConverter 指定了 数据库返回的一 Row 数据，怎么封装成 TBook
    //3）、配置 R2dbcCustomConversions 组件，让 BookConverter 加入其中生效
    @Override
    public TBookAuthor convert(Row source) {

        if(source == null) return null;
        //自定义结果集的封装
        TBookAuthor tBook = new TBookAuthor();

        tBook.setId(source.get("id", Long.class));
        tBook.setPublisher(source.get("publisher", String.class));

        Long author_id = source.get("author_id", Long.class);
        tBook.setAuthorId(author_id);
        tBook.setCreateTime(source.get("create_time", Instant.class));


        //让 converter兼容更多的表结构处理
//        if (source.get("name",String.class)) {
            TAuthor tAuthor = new TAuthor();
            tAuthor.setId(author_id);
            tAuthor.setName(source.get("name", String.class));

            tBook.setTAuthor(tAuthor);
//        }
        return tBook;
    }
}
