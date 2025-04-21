package com.bookstore.service;

import com.bookstore.exception.AuthorNotFoundException;
import com.bookstore.exception.InvalidInputException;
import com.bookstore.model.Author;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class AuthorService {
    private final Map<Long, Author> authors = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public Author createAuthor(Author author) {
        if (author == null) {
            throw new InvalidInputException("Author cannot be null");
        }
        if (author.getName() == null || author.getName().trim().isEmpty()) {
            throw new InvalidInputException("Author name cannot be empty");
        }

        author.setId(idGenerator.getAndIncrement());
        authors.put(author.getId(), author);
        return author;
    }

    public Author getAuthor(Long id) {
        if (id == null) {
            throw new InvalidInputException("Author ID cannot be null");
        }
        Author author = authors.get(id);
        if (author == null) {
            throw new AuthorNotFoundException(id);
        }
        return author;
    }

    public List<Author> getAllAuthors() {
        return new ArrayList<>(authors.values());
    }

    public Author updateAuthor(Long id, Author author) {
        if (id == null || author == null) {
            throw new InvalidInputException("Author ID and author data cannot be null");
        }
        if (!authors.containsKey(id)) {
            throw new AuthorNotFoundException(id);
        }

        author.setId(id);
        authors.put(id, author);
        return author;
    }

    public void deleteAuthor(Long id) {
        if (id == null) {
            throw new InvalidInputException("Author ID cannot be null");
        }
        if (!authors.containsKey(id)) {
            throw new AuthorNotFoundException(id);
        }
        authors.remove(id);
    }

    public List<Author> searchAuthorsByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidInputException("Search name cannot be empty");
        }

        return authors.values().stream()
                .filter(author -> author.getName().toLowerCase().contains(name.toLowerCase()))
                .toList();
    }
}