package com.bookstore.service;

import com.bookstore.exception.BookNotFoundException;
import com.bookstore.exception.InvalidInputException;
import com.bookstore.model.Book;
import com.bookstore.model.Author;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class BookService {
    private static final Map<Long, Book> books = new ConcurrentHashMap<>();
    private static final AtomicLong idGenerator = new AtomicLong(1);
    private AuthorService authorService;

    public BookService() {
        // Remove AuthorService initialization from constructor
    }

    // Add setter for AuthorService
    public void setAuthorService(AuthorService authorService) {
        this.authorService = authorService;
    }

    public Book createBook(Book book) {
        if (book == null) {
            throw new InvalidInputException("Book cannot be null");
        }
        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            throw new InvalidInputException("Book title cannot be empty");
        }
        if (book.getAuthor() == null || book.getAuthor().getId() == null) {
            throw new InvalidInputException("Author ID is required");
        }

        // Check if authorService is initialized
        if (authorService != null) {
            Author author = authorService.getAuthor(book.getAuthor().getId());
            book.setAuthor(author);
        }

        book.setId(idGenerator.getAndIncrement());
        books.put(book.getId(), book);
        return book;
    }

    public Book getBook(Long id) {
        if (id == null) {
            throw new InvalidInputException("Book ID cannot be null");
        }
        Book book = books.get(id);
        if (book == null) {
            throw new BookNotFoundException(id);
        }
        return book;
    }

    public List<Book> getAllBooks() {
        return new ArrayList<>(books.values());
    }

    public Book updateBook(Long id, Book book) {
        if (id == null || book == null) {
            throw new InvalidInputException("Book ID and book data cannot be null");
        }
        if (!books.containsKey(id)) {
            throw new BookNotFoundException(id);
        }

        book.setId(id);
        books.put(id, book);
        return book;
    }

    public void deleteBook(Long id) {
        if (id == null) {
            throw new InvalidInputException("Book ID cannot be null");
        }
        if (!books.containsKey(id)) {
            throw new BookNotFoundException(id);
        }
        books.remove(id);
    }

    public List<Book> searchBooksByTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new InvalidInputException("Search title cannot be empty");
        }

        return books.values().stream()
                .filter(book -> book.getTitle().toLowerCase().contains(title.toLowerCase()))
                .toList();
    }

    public boolean updateStock(Long id, int quantity) {
        Book book = getBook(id);
        if (quantity < 0 && Math.abs(quantity) > book.getStockQuantity()) {
            return false;
        }
        book.setStockQuantity(book.getStockQuantity() + quantity);
        return true;
    }
}