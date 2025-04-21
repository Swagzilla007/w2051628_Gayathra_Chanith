package com.bookstore.resource;

import com.bookstore.model.Author;
import com.bookstore.service.AuthorService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/authors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthorResource {
    private final AuthorService authorService;

    public AuthorResource() {
        this.authorService = new AuthorService();
    }

    @POST
    public Response createAuthor(Author author) {
        Author createdAuthor = authorService.createAuthor(author);
        return Response.status(Response.Status.CREATED)
                .entity(createdAuthor)
                .build();
    }

    @GET
    @Path("/{id}")
    public Response getAuthor(@PathParam("id") Long id) {
        Author author = authorService.getAuthor(id);
        return Response.ok(author).build();
    }

    @GET
    public Response getAllAuthors() {
        List<Author> authors = authorService.getAllAuthors();
        return Response.ok(authors).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateAuthor(@PathParam("id") Long id, Author author) {
        Author updatedAuthor = authorService.updateAuthor(id, author);
        return Response.ok(updatedAuthor).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteAuthor(@PathParam("id") Long id) {
        authorService.deleteAuthor(id);
        return Response.noContent().build();
    }

    @GET
    @Path("/search")
    public Response searchAuthors(@QueryParam("name") String name) {
        List<Author> authors = authorService.searchAuthorsByName(name);
        return Response.ok(authors).build();
    }
}