package com.lms.lms.borrowedbooks;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BorrowedBooksRepository extends JpaRepository<BorrowedBooks, BorrowedBooksId> {

    @Query("SELECT X FROM BorrowedBooks X WHERE X.borrowedBooksId.appUserId=?1 AND " +
            "X.returnedDate=NULL AND X.endDate<?2")
    List<BorrowedBooks> findUserUnreturnedBooks(Long appUserId, LocalDate now);
    @Query("SELECT X FROM BorrowedBooks X WHERE X.returnedDate=NULL AND X.endDate<=:nowDate")
    List<BorrowedBooks> findAllUnreturnedBooks(@Param("nowDate") LocalDate now);

    @Query("SELECT x FROM BorrowedBooks x WHERE x.borrowedBooksId.bookId=?1 AND " +
            "x.borrowedBooksId.appUserId=?2 " +
            "AND x.returnedDate=null")
    Optional<BorrowedBooks> getBorrowedBook(Long bookId, Long appUserId);

    @Query("SELECT x FROM BorrowedBooks x WHERE x.borrowedBooksId.appUserId=?1 " +
            "AND x.returnedDate=null")
    List<BorrowedBooks> getAllBorrowedBooksByUserNow(Long appUserId);

    @Query("SELECT x FROM BorrowedBooks x WHERE x.borrowedBooksId.appUserId=?1")
    List<BorrowedBooks> getAllBorrowedBooksByUser(Long appUserId);
}
