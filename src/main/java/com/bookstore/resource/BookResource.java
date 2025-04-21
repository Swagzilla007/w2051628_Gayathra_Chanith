package com.bookstore.resource;

import com.bookstore.model.Book;
import com.bookstore.service.BookService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {
    private final BookService bookService;

    public BookResource() {
        this.bookService = new BookService();
    }

    @POST
    public Response createBook(Book book) {
        Book createdBook = bookService.createBook(book);
        return Response.status(Response.Status.CREATED)
                .entity(createdBook)
                .build();
    }

    @GET
    @Path("/{id}")
    public Response getBook(@PathParam("id") Long id) {
        Book book = bookService.getBook(id);
        return Response.ok(book).build();
    }

    @GET
    public Response getAllBooks() {
        List<Book> books = bookService.getAllBooks();
        return Response.ok(books).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateBook(@PathParam("id") Long id, Book book) {
        Book updatedBook = bookService.updateBook(id, book);
        return Response.ok(updatedBook).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteBook(@PathParam("id") Long id) {
        bookService.deleteBook(id);
        return Response.noContent().build();
    }

    @GET
    @Path("/search")
    public Response searchBooks(@QueryParam("title") String title) {
        List<Book> books = bookService.searchBooksByTitle(title);
        return Response.ok(books).build();
    }

    @PUT
    @Path("/{id}/stock")
    public Response updateStock(@PathParam("id") Long id, @QueryParam("quantity") int quantity) {
        boolean updated = bookService.updateStock(id, quantity);
        if (updated) {
            return Response.ok().build();
        }
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("Cannot update stock. Insufficient quantity available.")
                .build();
    }
}