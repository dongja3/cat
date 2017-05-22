package com.dianping.cat.eclipselink.entity;

import javax.persistence.*;

@Entity
@Table(name="A_Computer")
public class Computer {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="Id")
    
    private Long id = null; 
    @Column(name="Name", length=20, nullable=false)
    private String name;
    
    @ManyToOne
    @JoinColumn(name="AuthorId")
    private Author author;

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

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }
    
    
}
