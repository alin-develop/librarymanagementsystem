package com.lms.lms.borrowedbooks;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BorrowedBooksId implements Serializable {

//    private static final long serialVersionUID = -5950169333088102831L;
    @Column(nullable = false, name = "app_user_id")
    private Long appUserId;

    @Column(nullable = false, name = "book_id")
    private Long bookId;

    @Column(name = "start_date", nullable = false) //When the book was borrowed
    private LocalDate startDate;

    public BorrowedBooksId(Long appUserId, Long bookId) {
        this.appUserId = appUserId;
        this.bookId = bookId;
        this.startDate = LocalDate.now();
    }
}
