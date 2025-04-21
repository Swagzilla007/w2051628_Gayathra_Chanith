package com.bookstore.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CartItem {
    private Book book;
    private int quantity;
    private double totalPrice;

    public CartItem() {
        this.quantity = 0;
        this.totalPrice = 0.0;
    }

    public CartItem(Book book, int quantity) {
        this.book = book;
        this.quantity = quantity;
        this.totalPrice = calculateTotalPrice();
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
        this.totalPrice = calculateTotalPrice();
    }

    public double getTotalPrice() {
        return calculateTotalPrice();
    }

    private double calculateTotalPrice() {
        if (book != null) {
            return book.getPrice() * quantity;
        }
        return 0.0;
    }
}