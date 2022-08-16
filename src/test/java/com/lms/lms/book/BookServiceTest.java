package com.lms.lms.book;

import com.lms.lms.category.CategoryRepository;
import com.lms.lms.category.CategoryService;
import com.lms.lms.error.BookAlreadyExistsException;
import com.lms.lms.error.BookNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookServiceTest {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    private BookService underTest;

    Book book1 = new Book("9780136019710", "Chemistry: An Introduction to General, Organic, & Biological Chemistry (10th Edition)",
            "Timberlake, Karen C.", 2008, 3, 20);
    Book book2 = new Book("9780136019711", "One Flew Over the Cuckoo's Nest", "Ken Kesey",
            1962, 1);
    BookRequest addbook1 = new BookRequest("9780136019710", "Chemistry: An Introduction to General, Organic, & Biological Chemistry (10th Edition)",
            "Timberlake, Karen C.", 2008, 3, 20);
    BookRequest addbook2 = new BookRequest("9780136019711", "One Flew Over the Cuckoo's Nest", "Ken Kesey",
            1962, 1);

    @BeforeEach
    void setUp() {
        CategoryService categoryService = new CategoryService(categoryRepository);
        underTest = new BookService(bookRepository, categoryService);
    }

    @Test
    void getBooksTest() {
        bookRepository.save(book1);
        List<Book> books = underTest.getBooks();
        assertNotNull(books);
    }

    @Test
    void addBook_givenBookAlreadyExists() {
        bookRepository.save(book1);
        assertThrows(BookAlreadyExistsException.class, () -> underTest.addBook(addbook1));
    }


    @Test
    void getBookById_givenBookExists() {
        Book save = bookRepository.save(book1);

        Book bookById = underTest.getBookById(save.getId());
        assertEquals(save.getId(), bookById.getId());
        assertEquals(save.getIsbn(), bookById.getIsbn());
    }

    @Test
    void getBookById_givenBookDoesNotExist() {
        Long idDoesNotExist = 12389458934L;
        assertThrows(BookNotFoundException.class, () -> underTest.getBookById(idDoesNotExist));
    }

    @Test
    void updateBook_givenBookDoesNotExist() {
        Long idDoesNotExist = 12389458934L;
        assertThrows(BookNotFoundException.class, () -> underTest.updateBook(idDoesNotExist,null,
                null,null,null,null));
    }

    @Test
    void updateBookByIdWithName() {
        Book save = bookRepository.save(book1);
        String name = book1.getName();

        Book newBook = underTest.updateBook(save.getId(), "Name of The Book", null,
                null, null, null);
        assertNotEquals(name, newBook.getName());
    }

    @Test
    void updateBookByIdWithAuthor() {
        bookRepository.save(book1);
        String author = book1.getAuthor();

        Book newBook = underTest.updateBook(book1.getId(), null, "Author Of The Book",
                null, null, null);
        assertNotEquals(author, newBook.getAuthor());
    }

    @Test
    void updateBookByIdWithYearPublished() {
        bookRepository.save(book1);
        Integer year = book1.getYearPublished();

        Book newBook = underTest.updateBook(book1.getId(), null, null,
                2022, null, null);
        assertNotEquals(year, newBook.getYearPublished());
    }

    @Test
    void updateBookByIdWithTotalAmount() {
        bookRepository.save(book1);
        Integer totalAmount = book1.getTotalAmount();

        Book newBook = underTest.updateBook(book1.getId(), null, null,
                null, 0, null);
        assertNotEquals(totalAmount, newBook.getTotalAmount());
    }

    @Test
    void updateBookByIdWithAmountAvailable() {
        bookRepository.save(book1);
        Integer amountAvailable = book1.getAmountAvailable();

        Book newBook = underTest.updateBook(book1.getId(), null, null,
                null, null, 2);
        assertNotEquals(amountAvailable, newBook.getAmountAvailable());
    }

    @AfterEach
    void tearDown() {
        bookRepository.deleteAll();
    }
}