package com.bookstore.resource;

import com.bookstore.model.Cart;
import com.bookstore.service.CartService;
import com.bookstore.service.BookService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/carts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CartResource {
    private final CartService cartService;

    public CartResource() {
        this.cartService = new CartService(new BookService());
    }

    @POST
    public Response createCart(Cart cart) {
        Cart createdCart = cartService.createCart(cart);
        return Response.status(Response.Status.CREATED)
                .entity(createdCart)
                .build();
    }

    @GET
    @Path("/{id}")
    public Response getCart(@PathParam("id") Long id) {
        Cart cart = cartService.getCart(id);
        return Response.ok(cart).build();
    }

    @GET
    public Response getAllCarts() {
        List<Cart> carts = cartService.getAllCarts();
        return Response.ok(carts).build();
    }

    @POST
    @Path("/{cartId}/items")
    public Response addItemToCart(
            @PathParam("cartId") Long cartId,
            @QueryParam("bookId") Long bookId,
            @QueryParam("quantity") int quantity) {
        Cart updatedCart = cartService.addItemToCart(cartId, bookId, quantity);
        return Response.ok(updatedCart).build();
    }

    @DELETE
    @Path("/{cartId}/items/{bookId}")
    public Response removeItemFromCart(
            @PathParam("cartId") Long cartId,
            @PathParam("bookId") Long bookId) {
        Cart updatedCart = cartService.removeItemFromCart(cartId, bookId);
        return Response.ok(updatedCart).build();
    }

    @PUT
    @Path("/{cartId}/items/{bookId}")
    public Response updateItemQuantity(
            @PathParam("cartId") Long cartId,
            @PathParam("bookId") Long bookId,
            @QueryParam("quantity") int quantity) {
        Cart updatedCart = cartService.updateItemQuantity(cartId, bookId, quantity);
        return Response.ok(updatedCart).build();
    }

    @DELETE
    @Path("/{cartId}/items")
    public Response clearCart(@PathParam("cartId") Long cartId) {
        cartService.clearCart(cartId);
        return Response.noContent().build();
    }

    @GET
    @Path("/customer/{customerId}")
    public Response getCartsByCustomer(@PathParam("customerId") Long customerId) {
        List<Cart> carts = cartService.getCartsByCustomerId(customerId);
        return Response.ok(carts).build();
    }
}