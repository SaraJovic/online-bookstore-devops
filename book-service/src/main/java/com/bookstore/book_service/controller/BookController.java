package com.bookstore.book_service.controller;

import com.bookstore.book_service.dto.BookRequest;
import com.bookstore.book_service.dto.BookResponse;
import com.bookstore.book_service.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
@Slf4j
public class BookController {

    private final BookService bookService;

    @GetMapping
    public Flux<BookResponse> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<BookResponse>> getBookById(@PathVariable Long id) {
        return bookService.getBookById(id)
                .map(ResponseEntity::ok)
                .onErrorReturn(ResponseEntity.notFound().build());
    }

    @GetMapping("/category/{category}")
    public Flux<BookResponse> getBooksByCategory(@PathVariable String category) {
        return bookService.getBooksByCategory(category);
    }

    @GetMapping("/search")
    public Flux<BookResponse> searchBooks(@RequestParam String title) {
        return bookService.searchBooks(title);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<BookResponse> createBook(@Valid @RequestBody BookRequest request) {
        return bookService.createBook(request);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<BookResponse>> updateBook(@PathVariable Long id,
                                                         @Valid @RequestBody BookRequest request) {
        return bookService.updateBook(id, request)
                .map(ResponseEntity::ok)
                .onErrorReturn(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteBook(@PathVariable Long id) {
        return bookService.deleteBook(id);
    }

    @GetMapping("/health")
    public Mono<String> health() {
        return Mono.just("Book Service is running!");
    }
}