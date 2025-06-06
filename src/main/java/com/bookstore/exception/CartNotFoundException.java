package com.bookstore.exception;

public class CartNotFoundException extends RuntimeException {
    public CartNotFoundException(String message) {
        super(message);
    }

    public CartNotFoundException(Long id) {
        super("Cart not found with id: " + id);
    }
}