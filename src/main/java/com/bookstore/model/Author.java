package com.bookstore.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Author {
    private Long id;
    private String name;
    private String biography;

    public Author() {
        // Default constructor required for JAX-RS
    }

    public Author(Long id, String name, String biography) {
        this.id = id;
        this.name = name;
        this.biography = biography;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }
}