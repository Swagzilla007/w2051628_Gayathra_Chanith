package com.bookstore.service;

import com.bookstore.exception.CustomerNotFoundException;
import com.bookstore.exception.InvalidInputException;
import com.bookstore.model.Customer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class CustomerService {
    private final Map<Long, Customer> customers = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public Customer createCustomer(Customer customer) {
        if (customer == null) {
            throw new InvalidInputException("Customer cannot be null");
        }
        if (customer.getName() == null || customer.getName().trim().isEmpty()) {
            throw new InvalidInputException("Customer name cannot be empty");
        }
        if (customer.getEmail() == null || customer.getEmail().trim().isEmpty()) {
            throw new InvalidInputException("Customer email cannot be empty");
        }
        if (customer.getPassword() == null || customer.getPassword().trim().isEmpty()) {
            throw new InvalidInputException("Customer password cannot be empty");
        }

        // Basic email validation
        if (!customer.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new InvalidInputException("Invalid email format");
        }

        customer.setId(idGenerator.getAndIncrement());
        customers.put(customer.getId(), customer);
        return customer;
    }

    public Customer getCustomer(Long id) {
        if (id == null) {
            throw new InvalidInputException("Customer ID cannot be null");
        }
        Customer customer = customers.get(id);
        if (customer == null) {
            throw new CustomerNotFoundException(id);
        }
        return customer;
    }

    public List<Customer> getAllCustomers() {
        return new ArrayList<>(customers.values());
    }

    public Customer updateCustomer(Long id, Customer customer) {
        if (id == null || customer == null) {
            throw new InvalidInputException("Customer ID and customer data cannot be null");
        }
        if (!customers.containsKey(id)) {
            throw new CustomerNotFoundException(id);
        }

        customer.setId(id);
        customers.put(id, customer);
        return customer;
    }

    public void deleteCustomer(Long id) {
        if (id == null) {
            throw new InvalidInputException("Customer ID cannot be null");
        }
        if (!customers.containsKey(id)) {
            throw new CustomerNotFoundException(id);
        }
        customers.remove(id);
    }

    public Customer findByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new InvalidInputException("Email cannot be empty");
        }

        return customers.values().stream()
                .filter(customer -> customer.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with email: " + email));
    }
}