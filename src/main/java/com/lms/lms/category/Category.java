package com.lms.lms.category;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lms.lms.book.Book;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "category")
@Getter
@Setter
public class Category {
    @Id
    @SequenceGenerator(name = "category_sequence",
    sequenceName = "category_sequence",
    initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
    generator = "category_sequence")
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(fetch = FetchType.EAGER,
            mappedBy = "parent",
            cascade = CascadeType.REMOVE,
            orphanRemoval = true)
    @JsonIgnore
    private List<Category> children;

    @ManyToMany(mappedBy = "categories", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Book> books;

    public Category(String name, Category parent) {
        this.name = name;
        this.parent = parent;
    }

    public Category() {

    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public void addChild(Category child){
        this.getChildren().add(child);
    }

    public void removeChild(Category child){
        this.getChildren().remove(child);
    }

    public void addBook(Book book){
        this.getBooks().add(book);
    }

    public void removeBook(Book book){
        this.getBooks().remove(book);
    }

}
