package com.bookstore.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class Order {
    private Long id;
    private Customer customer;
    private List<CartItem> items;
    private String orderDate; // Changed to String
    private double totalAmount;
    private String status;

    public Order() {
        items = new ArrayList<>();
        this.orderDate = "2024-04-22"; // Hardcoded date
    }

    public Order(Long id, Customer customer, List<CartItem> items) {
        this.id = id;
        this.customer = customer;
        this.items = items;
        this.orderDate = "2024-04-22"; // Hardcoded date
        this.totalAmount = calculateTotalAmount();
        this.status = "PENDING";
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
        this.totalAmount = calculateTotalAmount();
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Helper methods
    private double calculateTotalAmount() {
        return items.stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();
    }
}