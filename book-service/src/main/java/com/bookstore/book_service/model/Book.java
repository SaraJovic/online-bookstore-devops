package com.bookstore.book_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("books")
public class Book {

    @Id
    private Long id;

    private String title;

    private String author;

    private String isbn;

    private String description;

    private BigDecimal price;

    private Integer stock;

    private String category;

    @Column("created_at")
    private LocalDateTime createdAt;
}