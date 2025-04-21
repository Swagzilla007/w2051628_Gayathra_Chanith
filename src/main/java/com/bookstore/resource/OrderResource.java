package com.bookstore.resource;

import com.bookstore.model.Order;
import com.bookstore.service.OrderService;
import com.bookstore.service.CartService;
import com.bookstore.service.BookService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {
    private final OrderService orderService;

    public OrderResource() {
        BookService bookService = new BookService();
        CartService cartService = new CartService(bookService);
        this.orderService = new OrderService(cartService, bookService);
    }

    @POST
    @Path("/cart/{cartId}")
    public Response createOrderFromCart(@PathParam("cartId") Long cartId) {
        Order order = orderService.createOrderFromCart(cartId);
        return Response.status(Response.Status.CREATED)
                .entity(order)
                .build();
    }

    @GET
    @Path("/{id}")
    public Response getOrder(@PathParam("id") Long id) {
        Order order = orderService.getOrder(id);
        return Response.ok(order).build();
    }

    @GET
    public Response getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return Response.ok(orders).build();
    }

    @GET
    @Path("/customer/{customerId}")
    public Response getCustomerOrders(@PathParam("customerId") Long customerId) {
        List<Order> orders = orderService.getOrdersByCustomerId(customerId);
        return Response.ok(orders).build();
    }

    @PUT
    @Path("/{orderId}/status")
    public Response updateOrderStatus(
            @PathParam("orderId") Long orderId,
            @QueryParam("status") String status) {
        Order updatedOrder = orderService.updateOrderStatus(orderId, status);
        return Response.ok(updatedOrder).build();
    }

    @POST
    @Path("/{orderId}/cancel")
    public Response cancelOrder(@PathParam("orderId") Long orderId) {
        orderService.cancelOrder(orderId);
        return Response.ok().build();
    }
}