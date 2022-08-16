package com.lms.lms.book;

import com.lms.lms.appuser.AppUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    Book book1 = new Book("9780136019709", "Chemistry: An Introduction to General, Organic, & Biological Chemistry (10th Edition)",
            "Timberlake, Karen C.", 2008, 3, 20);
    Book book2 = new Book("9780136019708", "One Flew Over the Cuckoo's Nest", "Ken Kesey",
            1962, 1);

    @Test
    void findByIsbn_givenBookExistsInDatabase_Return() {
        //given
        bookRepository.save(book1);
        //when
        Optional<Book> byIsbn = bookRepository.findBookByIsbn(book1.getIsbn());
        //then
        assertNotNull(byIsbn);
        assertEquals(book1.getIsbn(), byIsbn.get().getIsbn());
    }

    @Test
    void findByIsbn_givenBookDoesNotExistInDatabase_ThrowException() {
        //given

        //when
        Optional<Book> byIsbn = bookRepository.findBookByIsbn(book2.getIsbn());
        //then
        assertEquals(Optional.empty(), byIsbn);
    }

}
