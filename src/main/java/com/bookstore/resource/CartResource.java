package com.bookstore.resource;

import com.bookstore.model.Cart;
import com.bookstore.service.CartService;
import com.bookstore.service.BookService;
import com.bookstore.service.CustomerService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/customers/{customerId}/cart")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CartResource {
    private final CartService cartService;
    private final CustomerService customerService;

    public CartResource() {
        BookService bookService = new BookService();
        this.customerService = new CustomerService();
        this.cartService = new CartService(bookService);
    }

    @POST
    @Path("/items")
    public Response addItemToCart(
            @PathParam("customerId") Long customerId,
            @QueryParam("bookId") Long bookId,
            @QueryParam("quantity") int quantity) {
        // Verify customer exists
        customerService.getCustomer(customerId);
        Cart cart = cartService.addItemToCart(customerId, bookId, quantity);
        return Response.ok(cart).build();
    }

    @GET
    public Response getCustomerCart(@PathParam("customerId") Long customerId) {
        // Verify customer exists
        customerService.getCustomer(customerId);
        Cart cart = cartService.getCartByCustomerId(customerId);
        return Response.ok(cart).build();
    }

    @PUT
    @Path("/items/{bookId}")
    public Response updateCartItem(
            @PathParam("customerId") Long customerId,
            @PathParam("bookId") Long bookId,
            @QueryParam("quantity") int quantity) {
        // Verify customer exists
        customerService.getCustomer(customerId);
        Cart cart = cartService.updateItemQuantity(customerId, bookId, quantity);
        return Response.ok(cart).build();
    }

    @DELETE
    @Path("/items/{bookId}")
    public Response removeCartItem(
            @PathParam("customerId") Long customerId,
            @PathParam("bookId") Long bookId) {
        // Verify customer exists
        customerService.getCustomer(customerId);
        Cart cart = cartService.removeItemFromCart(customerId, bookId);
        return Response.ok(cart).build();
    }
}