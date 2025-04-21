package com.bookstore.service;

import com.bookstore.exception.InvalidInputException;
import com.bookstore.exception.OutOfStockException;
import com.bookstore.model.Order;
import com.bookstore.model.Cart;
import com.bookstore.model.CartItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class OrderService {
    private static final Map<Long, Order> orders = new ConcurrentHashMap<>();
    private static final AtomicLong idGenerator = new AtomicLong(1);
    private final CartService cartService; // Remove static
    private final BookService bookService; // Remove static

    public OrderService(CartService cartService, BookService bookService) {
        this.cartService = cartService;
        this.bookService = bookService;
    }

    public Order createOrderFromCart(Long cartId) {
        Cart cart = cartService.getCart(cartId);

        if (cart.getItems().isEmpty()) {
            throw new InvalidInputException("Cannot create order from empty cart");
        }

        // Verify stock availability for all items
        for (CartItem item : cart.getItems()) {
            if (item.getBook().getStockQuantity() < item.getQuantity()) {
                throw new OutOfStockException("Insufficient stock for book: " + item.getBook().getTitle());
            }
        }

        // Update stock quantities
        for (CartItem item : cart.getItems()) {
            bookService.updateStock(item.getBook().getId(), -item.getQuantity());
        }

        // Create new order
        Order order = new Order(idGenerator.getAndIncrement(), cart.getCustomer(), new ArrayList<>(cart.getItems()));
        orders.put(order.getId(), order);

        // Clear the cart after successful order creation
        cartService.clearCart(cartId);

        return order;
    }

    public Order getOrder(Long id) {
        if (id == null) {
            throw new InvalidInputException("Order ID cannot be null");
        }
        Order order = orders.get(id);
        if (order == null) {
            throw new InvalidInputException("Order not found with id: " + id);
        }
        return order;
    }

    public List<Order> getAllOrders() {
        return new ArrayList<>(orders.values());
    }

    public List<Order> getOrdersByCustomerId(Long customerId) {
        return orders.values().stream()
                .filter(order -> order.getCustomer().getId().equals(customerId))
                .toList();
    }

    public Order updateOrderStatus(Long orderId, String status) {
        Order order = getOrder(orderId);
        order.setStatus(status);
        return order;
    }

    public void cancelOrder(Long orderId) {
        Order order = getOrder(orderId);

        if (!"PENDING".equals(order.getStatus())) {
            throw new InvalidInputException("Can only cancel orders in PENDING status");
        }

        // Return items to inventory
        for (CartItem item : order.getItems()) {
            bookService.updateStock(item.getBook().getId(), item.getQuantity());
        }

        order.setStatus("CANCELLED");
    }
}