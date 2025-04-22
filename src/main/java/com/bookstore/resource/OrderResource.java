package com.bookstore.resource;

import com.bookstore.model.Order;
import com.bookstore.service.OrderService;
import com.bookstore.service.CartService;
import com.bookstore.service.BookService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/customers/{customerId}/orders")
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
    public Response createOrder(@PathParam("customerId") Long customerId) {
        Order order = orderService.createOrderFromCart(customerId);
        return Response.status(Response.Status.CREATED)
                .entity(order)
                .build();
    }

    @GET
    public Response getCustomerOrders(@PathParam("customerId") Long customerId) {
        List<Order> orders = orderService.getOrdersByCustomerId(customerId);
        return Response.ok(orders).build();
    }

    @GET
    @Path("/{orderId}")
    public Response getOrder(
            @PathParam("customerId") Long customerId,
            @PathParam("orderId") Long orderId) {
        Order order = orderService.getOrder(orderId);
        if (!order.getCustomer().getId().equals(customerId)) {
            throw new NotAuthorizedException("Order does not belong to customer");
        }
        return Response.ok(order).build();
    }
}