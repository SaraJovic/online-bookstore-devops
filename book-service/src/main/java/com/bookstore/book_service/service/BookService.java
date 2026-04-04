package com.bookstore.book_service.service;

import com.bookstore.book_service.dto.BookRequest;
import com.bookstore.book_service.dto.BookResponse;
import com.bookstore.book_service.model.Book;
import com.bookstore.book_service.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookService {

    private final BookRepository bookRepository;

    public Flux<BookResponse> getAllBooks() {
        log.info("Fetching all books");
        return bookRepository.findAll()
                .map(this::mapToResponse);
    }

    public Mono<BookResponse> getBookById(Long id) {
        log.info("Fetching book with id: {}", id);
        return bookRepository.findById(id)
                .map(this::mapToResponse)
                .switchIfEmpty(Mono.error(new RuntimeException("Book not found with id: " + id)));
    }

    public Flux<BookResponse> getBooksByCategory(String category) {
        log.info("Fetching books by category: {}", category);
        return bookRepository.findByCategory(category)
                .map(this::mapToResponse);
    }

    public Flux<BookResponse> searchBooks(String title) {
        log.info("Searching books by title: {}", title);
        return bookRepository.findByTitleContainingIgnoreCase(title)
                .map(this::mapToResponse);
    }

    public Mono<BookResponse> createBook(BookRequest request) {
        log.info("Creating book: {}", request.getTitle());
        Book book = Book.builder()
                .title(request.getTitle())
                .author(request.getAuthor())
                .isbn(request.getIsbn())
                .description(request.getDescription())
                .price(request.getPrice())
                .stock(request.getStock())
                .category(request.getCategory())
                .createdAt(LocalDateTime.now())
                .build();

        return bookRepository.save(book)
                .map(this::mapToResponse);
    }

    public Mono<BookResponse> updateBook(Long id, BookRequest request) {
        log.info("Updating book with id: {}", id);
        return bookRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Book not found with id: " + id)))
                .flatMap(book -> {
                    book.setTitle(request.getTitle());
                    book.setAuthor(request.getAuthor());
                    book.setIsbn(request.getIsbn());
                    book.setDescription(request.getDescription());
                    book.setPrice(request.getPrice());
                    book.setStock(request.getStock());
                    book.setCategory(request.getCategory());
                    return bookRepository.save(book);
                })
                .map(this::mapToResponse);
    }

    public Mono<Void> deleteBook(Long id) {
        log.info("Deleting book with id: {}", id);
        return bookRepository.deleteById(id);
    }

    private BookResponse mapToResponse(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .isbn(book.getIsbn())
                .description(book.getDescription())
                .price(book.getPrice())
                .stock(book.getStock())
                .category(book.getCategory())
                .createdAt(book.getCreatedAt())
                .build();
    }
}