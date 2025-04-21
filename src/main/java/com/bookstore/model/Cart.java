package com.bookstore.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class Cart {
    private Long id;
    private Customer customer;
    private List<CartItem> items;
    private double totalPrice;

    public Cart() {
        this.items = new ArrayList<>();
        this.totalPrice = 0.0;
    }

    public Cart(Long id, Customer customer) {
        this.id = id;
        this.customer = customer;
        this.items = new ArrayList<>();
        this.totalPrice = 0.0;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public double getTotalPrice() {
        this.totalPrice = calculateTotalPrice();
        return this.totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    // Helper methods
    public void addItem(CartItem item) {
        items.add(item);
        this.totalPrice = calculateTotalPrice();
    }

    public void removeItem(CartItem item) {
        items.remove(item);
        this.totalPrice = calculateTotalPrice();
    }

    public void clearCart() {
        items.clear();
        this.totalPrice = 0.0;
    }

    private double calculateTotalPrice() {
        return items.stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();
    }
}