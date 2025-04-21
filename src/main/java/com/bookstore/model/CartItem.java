package com.bookstore.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CartItem {
    private Book book;
    private int quantity;

    public CartItem() {
        // Default constructor required for JAX-RS
    }

    public CartItem(Book book, int quantity) {
        this.book = book;
        this.quantity = quantity;
    }

    // Getters and Setters
    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Helper method to calculate total price for this item
    public double getTotalPrice() {
        return book.getPrice() * quantity;
    }
}