package com.lms.lms.book;

import lombok.*;

@Getter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BookRequest {

    String isbn;
    String name;
    String author;
    int yearPublished;
    int totalAmount;
    int borrowingPeriod;
    int amountAvailable;

    public BookRequest(String isbn, String name, String author, int yearPublished, int totalAmount, int borrowingPeriod) {
        this.isbn = isbn;
        this.name = name;
        this.author = author;
        this.yearPublished = yearPublished;
        this.totalAmount = totalAmount;
        this.borrowingPeriod = borrowingPeriod;
    }

    public BookRequest(String isbn, String name, String author, int yearPublished, int totalAmount) {
        this.isbn = isbn;
        this.name = name;
        this.author = author;
        this.yearPublished = yearPublished;
        this.totalAmount = totalAmount;
    }

}
