package com.bookstore.service;

import com.bookstore.exception.CartNotFoundException;
import com.bookstore.exception.InvalidInputException;
import com.bookstore.exception.OutOfStockException;
import com.bookstore.model.Cart;
import com.bookstore.model.CartItem;
import com.bookstore.model.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class CartService {
    private final Map<Long, Cart> carts = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);
    private final BookService bookService;

    public CartService(BookService bookService) {
        this.bookService = bookService;
    }

    public Cart createCart(Cart cart) {
        if (cart == null) {
            throw new InvalidInputException("Cart cannot be null");
        }
        if (cart.getCustomer() == null) {
            throw new InvalidInputException("Cart must be associated with a customer");
        }

        cart.setId(idGenerator.getAndIncrement());
        carts.put(cart.getId(), cart);
        return cart;
    }

    public Cart getCart(Long id) {
        if (id == null) {
            throw new InvalidInputException("Cart ID cannot be null");
        }
        Cart cart = carts.get(id);
        if (cart == null) {
            throw new CartNotFoundException(id);
        }
        return cart;
    }

    public List<Cart> getAllCarts() {
        return new ArrayList<>(carts.values());
    }

    public Cart addItemToCart(Long cartId, Long bookId, int quantity) {
        if (quantity <= 0) {
            throw new InvalidInputException("Quantity must be greater than 0");
        }

        Cart cart = getCart(cartId);
        Book book = bookService.getBook(bookId);

        if (book.getStockQuantity() < quantity) {
            throw new OutOfStockException("Not enough stock available for book: " + book.getTitle());
        }

        // Check if the book is already in the cart
        CartItem existingItem = cart.getItems().stream()
                .filter(item -> item.getBook().getId().equals(bookId))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            // Update quantity of existing item
            int newQuantity = existingItem.getQuantity() + quantity;
            if (book.getStockQuantity() < newQuantity) {
                throw new OutOfStockException("Not enough stock available for book: " + book.getTitle());
            }
            existingItem.setQuantity(newQuantity);
        } else {
            // Add new item to cart
            cart.addItem(new CartItem(book, quantity));
        }

        return cart;
    }

    public Cart removeItemFromCart(Long cartId, Long bookId) {
        Cart cart = getCart(cartId);
        cart.getItems().removeIf(item -> item.getBook().getId().equals(bookId));
        return cart;
    }

    public Cart updateItemQuantity(Long cartId, Long bookId, int quantity) {
        if (quantity <= 0) {
            throw new InvalidInputException("Quantity must be greater than 0");
        }

        Cart cart = getCart(cartId);
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

    public void clearCart(Long cartId) {
        Cart cart = getCart(cartId);
        cart.clearCart();
    }

    public void deleteCart(Long id) {
        if (id == null) {
            throw new InvalidInputException("Cart ID cannot be null");
        }
        if (!carts.containsKey(id)) {
            throw new CartNotFoundException(id);
        }
        carts.remove(id);
    }

    public List<Cart> getCartsByCustomerId(Long customerId) {
        return carts.values().stream()
                .filter(cart -> cart.getCustomer().getId().equals(customerId))
                .toList();
    }
}