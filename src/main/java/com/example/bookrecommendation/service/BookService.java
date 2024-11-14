package com.example.bookrecommendation.service;

import com.example.bookrecommendation.model.Book;
import com.example.bookrecommendation.repository.BookRepository;
import com.example.bookrecommendation.dto.BookRequest;
import com.example.bookrecommendation.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public Book createBook(BookRequest bookRequest) {

        Book book = new Book();
        book.setTitle(bookRequest.getTitle());
        book.setAuthor(bookRequest.getAuthor());
        book.setGenre(bookRequest.getGenre());
        book.setDescription(bookRequest.getDescription());

        return bookRepository.save(book);
    }

    public List<Book> getAllBooks() {

        return bookRepository.findAll();
    }

    public Book getBookById(Long id) {

        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("도서", "id", id));
    }

    public Book updateBook(Long id, BookRequest bookRequest) {

        Book book = getBookById(id);

        book.setTitle(bookRequest.getTitle());
        book.setAuthor(bookRequest.getAuthor());
        book.setGenre(bookRequest.getGenre());
        book.setDescription(bookRequest.getDescription());

        return bookRepository.save(book);
    }

    public void deleteBook(Long id) {

        Book book = getBookById(id);

        bookRepository.delete(book);
    }
}