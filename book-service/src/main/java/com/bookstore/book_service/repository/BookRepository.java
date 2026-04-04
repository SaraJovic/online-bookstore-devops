package com.bookstore.book_service.repository;

import com.bookstore.book_service.model.Book;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface BookRepository extends ReactiveCrudRepository<Book, Long> {
    Flux<Book> findByCategory(String category);
    Flux<Book> findByAuthor(String author);
    Mono<Book> findByIsbn(String isbn);
    Flux<Book> findByTitleContainingIgnoreCase(String title);
}