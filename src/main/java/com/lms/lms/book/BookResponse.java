package com.lms.lms.book;

import com.lms.lms.photo.Photo;
import lombok.Getter;

@Getter
public class BookResponse {
    private final Long id;
    private final String isbn;

    private final String name;

    private final String author;

    private final Integer yearPublished;

    private final Integer totalAmount;

    private final Integer borrowingPeriod;

    private final Integer amountAvailable;

    private final Photo photo;

    public BookResponse(Book book) {
        this.id = book.getId();
        this.isbn = book.getIsbn();
        this.name = book.getName();
        this.author = book.getAuthor();
        this.yearPublished = book.getYearPublished();
        this.totalAmount = book.getTotalAmount();
        this.borrowingPeriod = book.getBorrowingPeriod();
        this.amountAvailable = book.getAmountAvailable();
        this.photo = book.getPhoto();
    }
}
