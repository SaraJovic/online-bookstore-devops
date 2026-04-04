package com.bookstore.book_service;

import com.bookstore.book_service.dto.BookRequest;
import com.bookstore.book_service.dto.BookResponse;
import com.bookstore.book_service.model.Book;
import com.bookstore.book_service.repository.BookRepository;
import com.bookstore.book_service.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book book;
    private BookRequest bookRequest;

    @BeforeEach
    void setUp() {
        book = Book.builder()
                .id(1L)
                .title("Clean Code")
                .author("Robert Martin")
                .isbn("978-0132350884")
                .description("A handbook of agile software craftsmanship")
                .price(new BigDecimal("29.99"))
                .stock(10)
                .category("Programming")
                .createdAt(LocalDateTime.now())
                .build();

        bookRequest = new BookRequest();
        bookRequest.setTitle("Clean Code");
        bookRequest.setAuthor("Robert Martin");
        bookRequest.setIsbn("978-0132350884");
        bookRequest.setDescription("A handbook of agile software craftsmanship");
        bookRequest.setPrice(new BigDecimal("29.99"));
        bookRequest.setStock(10);
        bookRequest.setCategory("Programming");
    }

    @Test
    void getAllBooks_Success() {
        when(bookRepository.findAll()).thenReturn(Flux.just(book));

        StepVerifier.create(bookService.getAllBooks())
                .expectNextMatches(response ->
                        response.getTitle().equals("Clean Code") &&
                                response.getAuthor().equals("Robert Martin"))
                .verifyComplete();

        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void getBookById_Success() {
        when(bookRepository.findById(1L)).thenReturn(Mono.just(book));

        StepVerifier.create(bookService.getBookById(1L))
                .expectNextMatches(response -> response.getId().equals(1L))
                .verifyComplete();
    }

    @Test
    void getBookById_NotFound() {
        when(bookRepository.findById(99L)).thenReturn(Mono.empty());

        StepVerifier.create(bookService.getBookById(99L))
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void createBook_Success() {
        when(bookRepository.save(any(Book.class))).thenReturn(Mono.just(book));

        StepVerifier.create(bookService.createBook(bookRequest))
                .expectNextMatches(response ->
                        response.getTitle().equals("Clean Code"))
                .verifyComplete();

        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void deleteBook_Success() {
        when(bookRepository.deleteById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(bookService.deleteBook(1L))
                .verifyComplete();

        verify(bookRepository, times(1)).deleteById(1L);
    }

    @Test
    void getBooksByCategory_Success() {
        when(bookRepository.findByCategory("Programming")).thenReturn(Flux.just(book));

        StepVerifier.create(bookService.getBooksByCategory("Programming"))
                .expectNextMatches(response ->
                        response.getCategory().equals("Programming"))
                .verifyComplete();
    }
}