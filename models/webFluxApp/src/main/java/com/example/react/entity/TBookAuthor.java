package com.example.react.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Table("t_book")
@Data
public class TBookAuthor {
    @Schema(description = "主键")
    @Id
    Long id;

    @Schema(description = "作者")
    Long authorId;

    @Schema(description = "出版社")
    String publisher;

    /**
     * 响应式中日期的映射用 Instant 或者 LocalXxx
     */
    Instant createTime;



    TAuthor TAuthor;

}
