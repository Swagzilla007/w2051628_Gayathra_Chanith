package com.bookstore.exception;

public class OutOfStockException extends RuntimeException {
    public OutOfStockException(String message) {
        super(message);
    }

    public OutOfStockException(Long bookId, int requestedQuantity, int availableQuantity) {
        super(String.format("Book with id %d is out of stock. Requested: %d, Available: %d",
                bookId, requestedQuantity, availableQuantity));
    }
}