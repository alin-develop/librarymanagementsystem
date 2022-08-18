package com.lms.lms.borrowedbooks;

import com.lms.lms.appuser.AppUser;
import com.lms.lms.book.Book;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@ToString
public class BorrowedBooks {
    @EmbeddedId
    private BorrowedBooksId borrowedBooksId;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("appUserId")
    private AppUser appUser;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("bookId")
    private Book book;

    @Column(name = "end_date", nullable = false) //Deadline for returning the book
    private LocalDate endDate;

    @Column(name = "returned_date") //When the book was actually returned
    private LocalDate returnedDate;

    private Integer renewable = 2;//how many times are left for renewal of the period

    public BorrowedBooks(@NotNull BorrowedBooksId borrowedBooksId, AppUser appUser, Book book) {
        this.borrowedBooksId = borrowedBooksId;
        this.book = book;
        this.appUser = appUser;
        this.endDate = borrowedBooksId.getStartDate().plusDays(
                book.getBorrowingPeriod()
        );
    }

    public void setAppUser(@NotNull AppUser appUser) {
        this.borrowedBooksId.setAppUserId(appUser.getId());
        this.appUser = appUser;
    }

    public void setBook(@NotNull Book book) {
        this.borrowedBooksId.setBookId(book.getId());
        this.book = book;
    }

}
