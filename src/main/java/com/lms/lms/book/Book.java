package com.lms.lms.book;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lms.lms.borrowedbooks.BorrowedBooks;
import com.lms.lms.category.Category;
import com.lms.lms.photo.Photo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "book",
uniqueConstraints = {@UniqueConstraint(name = "isbn", columnNames = "isbn")}
)
@Getter
@Setter
@NoArgsConstructor
public class Book {
    @Id
    @SequenceGenerator(
            name = "book_sequence",
            sequenceName = "book_sequence",
            initialValue = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "book_sequence"
    )
    private Long id;
    private String isbn;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private Integer yearPublished;

    @Column(nullable = false)
    private Integer totalAmount;

    private Integer borrowingPeriod;

    private Integer amountAvailable;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "photo_id", referencedColumnName = "id")
    private Photo photo;

    @ManyToMany
    @JoinTable(
            name = "book_has_category",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    @JsonIgnore
    private List<Category> categories;

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<BorrowedBooks> borrowedBooks;


    public Book(String isbn, String name, String author, Integer yearPublished, Integer totalAmount) {
        this.isbn = isbn;
        this.name = name;
        this.author = author;
        this.yearPublished = yearPublished;
        this.totalAmount = totalAmount;
        this.amountAvailable = totalAmount;
        borrowingPeriod = 0;
    }

    public Book(String isbn, String name, String author, Integer yearPublished, Integer totalAmount, Integer borrowingPeriod) {
        this.isbn = isbn;
        this.name = name;
        this.author = author;
        this.yearPublished = yearPublished;
        this.totalAmount = totalAmount;
        this.amountAvailable = totalAmount;
        this.borrowingPeriod = (borrowingPeriod!=null) ? (borrowingPeriod) : 0;
    }


    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", isbn='" + isbn + '\'' +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", yearPublished=" + yearPublished +
                ", totalAmount=" + totalAmount +
                ", borrowingPeriod=" + borrowingPeriod +
                ", amountAvailable=" + amountAvailable +
                '}';
    }


    public void addBorrowedBooks(@NonNull BorrowedBooks borrowedBooks){
        this.getBorrowedBooks().add(borrowedBooks);
    }

}
