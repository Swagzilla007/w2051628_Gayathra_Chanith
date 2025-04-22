package com.bookstore.service;

import com.bookstore.exception.InvalidInputException;
import com.bookstore.exception.OutOfStockException;
import com.bookstore.model.Cart;
import com.bookstore.model.CartItem;
import com.bookstore.model.Book;
import com.bookstore.model.Customer;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CartService {
    private static final Map<Long, Cart> customerCarts = new ConcurrentHashMap<>();
    private final BookService bookService;

    public CartService(BookService bookService) {
        this.bookService = bookService;
    }

    public Cart getCartByCustomerId(Long customerId) {
        if (customerId == null) {
            throw new InvalidInputException("Customer ID cannot be null");
        }

        return customerCarts.computeIfAbsent(customerId, k -> {
            Cart cart = new Cart();
            cart.setId(customerId);
            cart.setItems(new ArrayList<>()); // Initialize items list
            cart.setCustomer(new Customer(customerId, null, null, null));
            return cart;
        });
    }

    public Cart addItemToCart(Long customerId, Long bookId, int quantity) {
        if (quantity <= 0) {
            throw new InvalidInputException("Quantity must be greater than 0");
        }

        Cart cart = getCartByCustomerId(customerId);
        Book book = bookService.getBook(bookId);

        if (book.getStockQuantity() < quantity) {
            throw new OutOfStockException("Not enough stock available for book: " + book.getTitle());
        }

        CartItem existingItem = cart.getItems().stream()
                .filter(item -> item.getBook().getId().equals(bookId))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            int newQuantity = existingItem.getQuantity() + quantity;
            if (book.getStockQuantity() < newQuantity) {
                throw new OutOfStockException("Not enough stock available for book: " + book.getTitle());
            }
            existingItem.setQuantity(newQuantity);
        } else {
            CartItem newItem = new CartItem(book, quantity);
            cart.addItem(newItem);
        }

        // Make sure to update the cart in the map
        customerCarts.put(customerId, cart);
        return cart;
    }

    public Cart removeItemFromCart(Long customerId, Long bookId) {
        Cart cart = getCartByCustomerId(customerId);
        cart.getItems().removeIf(item -> item.getBook().getId().equals(bookId));
        return cart;
    }

    public Cart updateItemQuantity(Long customerId, Long bookId, int quantity) {
        if (quantity <= 0) {
            throw new InvalidInputException("Quantity must be greater than 0");
        }

        Cart cart = getCartByCustomerId(customerId);
        Book book = bookService.getBook(bookId);

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getBook().getId().equals(bookId))
                .findFirst()
                .orElseThrow(() -> new InvalidInputException("Book not found in cart"));

        if (book.getStockQuantity() < quantity) {
            throw new OutOfStockException("Not enough stock available for book: " + book.getTitle());
        }

        item.setQuantity(quantity);
        return cart;
    }

    public void clearCart(Long customerId) {
        Cart cart = getCartByCustomerId(customerId);
        cart.clearCart();
    }
}