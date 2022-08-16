package com.lms.lms.borrowedbooks;

import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Data
public class BorrowedBooksResponse {
    private final Long appUserId;
    private final Long bookId;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final LocalDate returnedDate;

    public BorrowedBooksResponse(BorrowedBooks books) {
        this.appUserId = books.getBorrowedBooksId().getAppUserId();
        this.bookId = books.getBorrowedBooksId().getBookId();
        this.startDate = books.getBorrowedBooksId().getStartDate();
        this.endDate = books.getEndDate();
        this.returnedDate = books.getReturnedDate();
    }
}
