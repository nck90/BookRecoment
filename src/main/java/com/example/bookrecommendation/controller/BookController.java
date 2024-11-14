package com.example.bookrecommendation.controller;

import com.example.bookrecommendation.dto.BookRequest;
import com.example.bookrecommendation.model.Book;
import com.example.bookrecommendation.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @PostMapping
    public ResponseEntity<?> createBook(@Valid @RequestBody BookRequest bookRequest) {

        Book book = bookService.createBook(bookRequest);

        return ResponseEntity.ok(book);
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {

        List<Book> books = bookService.getAllBooks();

        return ResponseEntity.ok(books);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBookById(@PathVariable Long id) {

        Book book = bookService.getBookById(id);

        return ResponseEntity.ok(book);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(@PathVariable Long id, @Valid @RequestBody BookRequest bookRequest) {

        Book updatedBook = bookService.updateBook(id, bookRequest);

        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {

        bookService.deleteBook(id);

        return ResponseEntity.ok("도서가 삭제되었습니다.");
    }
}