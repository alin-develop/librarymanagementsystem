package com.lms.lms.borrowedbooks;

import com.lms.lms.appuser.AppUser;
import com.lms.lms.appuser.AppUserRepository;
import com.lms.lms.appuser.AppUserRole;
import com.lms.lms.book.Book;
import com.lms.lms.book.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class BorrowedBooksRepositoryTest {

    @Autowired private AppUserRepository appUserRepository;
    @Autowired private BookRepository bookRepository;
    @Autowired private BorrowedBooksRepository underTest;

    Book book1 = new Book("9780136019710", "Chemistry: An Introduction to General, Organic, & Biological Chemistry (10th Edition)",
            "Timberlake, Karen C.", 2008, 3, 20);
    Book book2 = new Book("9780136019711", "One Flew Over the Cuckoo's Nest", "Ken Kesey",
            1962, 1);
    AppUser user1 = new AppUser("Tessa", "Thompson", "tessathompson@gmail.com",
            "thompsontessa2022", AppUserRole.USER);

    @BeforeEach
    void setUp() {
        underTest.deleteAll();
        bookRepository.deleteAll();
        appUserRepository.deleteAll();
        bookRepository.save(book1);
        bookRepository.save(book2);
        appUserRepository.save(user1);
    }

    @Test
    void findUserUnreturnedBooks() {
        Optional<AppUser> user = appUserRepository.findByEmail(user1.getEmail());
        Optional<Book> book = bookRepository.findBookByIsbn(book1.getIsbn());
        BorrowedBooks borrowedBooks = new BorrowedBooks(new BorrowedBooksId(user.get().getId(), book.get().getId()),
                user.get(), book.get());
        borrowedBooks.getBorrowedBooksId().setStartDate(LocalDate.now().minusDays(20));
        borrowedBooks.setEndDate(LocalDate.now().minusDays(1));
        underTest.save(borrowedBooks);

        List<BorrowedBooks> userUnreturnedBooks = underTest.findUserUnreturnedBooks(user.get().getId(), LocalDate.now());
        assertNotNull(userUnreturnedBooks);
        assertEquals(1, userUnreturnedBooks.size());
        assertEquals(user.get().getId(), userUnreturnedBooks.get(0).getBorrowedBooksId().getAppUserId());
        assertTrue(userUnreturnedBooks.get(0).getEndDate().isBefore(LocalDate.now()));
    }

    @Test
    void findAllUnreturnedBooks() {
        Optional<AppUser> user = appUserRepository.findByEmail(user1.getEmail());
        Optional<Book> book = bookRepository.findBookByIsbn(book1.getIsbn());
        BorrowedBooks borrowedBooks = new BorrowedBooks(new BorrowedBooksId(user.get().getId(), book.get().getId()),
                user.get(), book.get());
        borrowedBooks.getBorrowedBooksId().setStartDate(LocalDate.now().minusDays(20));
        borrowedBooks.setEndDate(LocalDate.now().minusDays(1));
        underTest.save(borrowedBooks);

        List<BorrowedBooks> userUnreturnedBooks = underTest.findAllUnreturnedBooks(LocalDate.now());
        assertNotNull(userUnreturnedBooks);
        assertEquals(1, userUnreturnedBooks.size());
        assertTrue(userUnreturnedBooks.get(0).getEndDate().isBefore(LocalDate.now()));
    }

    @Test
    void getBorrowedBook() {
        Optional<AppUser> user = appUserRepository.findByEmail(user1.getEmail());
        Optional<Book> book = bookRepository.findBookByIsbn(book1.getIsbn());
        BorrowedBooks borrowedBooks = new BorrowedBooks(new BorrowedBooksId(user.get().getId(), book.get().getId()),
                user.get(), book.get());
        underTest.save(borrowedBooks);

        Optional<BorrowedBooks> borrowedBook = underTest.getBorrowedBook(book.get().getId(), user.get().getId());
        assertNotNull(borrowedBook.get());
        assertEquals(book.get().getId(), borrowedBook.get().getBorrowedBooksId().getBookId());
        assertEquals(user.get().getId(), borrowedBook.get().getBorrowedBooksId().getAppUserId());
        assertNull(borrowedBook.get().getReturnedDate());
    }

}