package com.lms.lms.borrowedbooks;

import com.lms.lms.appuser.AppUser;
import com.lms.lms.appuser.AppUserRole;
import com.lms.lms.appuser.AppUserService;
import com.lms.lms.book.Book;
import com.lms.lms.book.BookService;
import com.lms.lms.error.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class BorrowedBooksServiceTest {
    @Mock
    private BorrowedBooksRepository borrowedBooksRepository;
    @Mock
    private BookService bookService;
    @Mock
    private AppUserService appUserService;
    private BorrowedBooksService underTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        underTest = new BorrowedBooksService(borrowedBooksRepository, bookService, appUserService);
    }

    Book book1 = new Book("9780136019710", "Chemistry: An Introduction to General, Organic, & Biological Chemistry (10th Edition)",
            "Timberlake, Karen C.", 2008, 3, 20);

    AppUser user1 = new AppUser("Tessa", "Thompson", "tessathompson@gmail.com",
            "thompsontessa2022", AppUserRole.LIBRARIAN);



    @Test
    void getAllUnreturnedBooks() {
        //given
        List<BorrowedBooks> mock = List.of(mock(BorrowedBooks.class));
        given(borrowedBooksRepository.findAllUnreturnedBooks(any())).willReturn(mock);
        //when
        List<BorrowedBooks> allUnreturnedBooks = underTest.getAllUnreturnedBooks();
        //then
        assertEquals(mock, allUnreturnedBooks);
    }


    @Test
    void getAppUserUnreturnedBooks() {
        //given
        long id = 127484L;
        user1.setId(id);
        List<BorrowedBooks> mock = List.of(mock(BorrowedBooks.class));
        given(appUserService.getAppUserByUsername(user1.getEmail())).willReturn(user1);
        given(borrowedBooksRepository.findUserUnreturnedBooks(any(), any())).willReturn(mock);
        //when
        List<BorrowedBooks> appUserUnreturnedBooks = underTest.getAppUserUnreturnedBooks(user1.getEmail());
        //then
        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        then(borrowedBooksRepository).should().findUserUnreturnedBooks(captor.capture(), any());
        Long value = captor.getValue();
        assertThat(value).isEqualTo(id);
        assertEquals(mock, appUserUnreturnedBooks);
    }

    @Test
    void getBorrowedBook() {
        //given
        BorrowedBooks mock = mock(BorrowedBooks.class);
        given(borrowedBooksRepository.getBorrowedBook(any(), any())).willReturn(Optional.ofNullable(mock));
        //when
        BorrowedBooks borrowedBook = underTest.getBorrowedBook(anyLong(), anyLong());
        //then
        assertNotNull(borrowedBook);
        assertEquals(mock, borrowedBook);
    }

    @Test
    void getBorrowedBook_ifBookNotBorrowed() {
        //given
        given(borrowedBooksRepository.getBorrowedBook(any(), any())).willReturn(Optional.empty());
        //when
        //then
        assertThrows(BookNotBorrowedException.class, () -> underTest.getBorrowedBook(book1.getId(), user1.getId()) );
    }

    //TODO: CHECK THE CORRECTNESS HERE
    @ParameterizedTest
    @CsvSource({
            "12345,0",
            "0,102757",
            "0,0"
    })
    void borrowTheBook_ifNull(Long bookId, Long appUserId) {
        //given
        user1.setId(appUserId);
        book1.setId(bookId);
        Integer amountAvailable = book1.getAmountAvailable();
        //when
        assertThatThrownBy(() -> underTest.borrowTheBook(bookId, appUserId))
                .isInstanceOf(NullPointerException.class);
        then(borrowedBooksRepository).shouldHaveNoInteractions();
    }

    @Test
    void borrowTheBook_ifAppUserNotFound() {
        //given
        given(appUserService.getAppUserById(any())).willThrow(AppUserNotFoundException.class);
        given(bookService.getBookById(any())).willReturn(mock(Book.class));
        //when
        //then
        assertThrows(AppUserNotFoundException.class, ()-> underTest.borrowTheBook(1L, 2L));
    }

    @Test
    void borrowTheBook_ifBookNotFound() {
        //given
        given(bookService.getBookById(any())).willThrow(BookNotFoundException.class);
        //when
        //then
        assertThrows(BookNotFoundException.class, ()-> underTest.borrowTheBook(1L, 2L));
    }

    @Test
    void borrowTheBook_ifAmountAvailableLessThanOne() {
        //given
        book1.setAmountAvailable(0);
        given(bookService.getBookById(any())).willReturn(book1);
        given(appUserService.getAppUserById(any())).willReturn(user1);
        //when
        //then
        assertThrows(BookNotAvailableException.class, ()-> underTest.borrowTheBook(1L, 2L));

        book1.setAmountAvailable(book1.getTotalAmount());
    }

    @Test
    void borrowTheBook_ifBorrowingPeriodLessThanOne() {
        //given
        book1.setBorrowingPeriod(0);
        given(bookService.getBookById(any())).willReturn(book1);
        given(appUserService.getAppUserById(any())).willReturn(user1);
        //when
        //then
        assertThrows(BookNotAvailableException.class, ()-> underTest.borrowTheBook(1L, 2L));
        book1.setBorrowingPeriod(20);
    }

    @Test
    void borrowTheBook_ifBooksUnreturnedAfterDeadline() {
        //given
        given(bookService.getBookById(any())).willReturn(book1);
        given(appUserService.getAppUserById(any())).willReturn(user1);
        given(appUserService.getAppUserByUsername(any())).willReturn(user1);
        given(borrowedBooksRepository.findUserUnreturnedBooks(any(), any())).willReturn(List.of(mock(BorrowedBooks.class)));
        //when
        //then
        assertThrows(BookNotAvailableException.class, ()-> underTest.borrowTheBook(1L, 2L));
    }

    @Test
    void borrowTheBook_ifValid() {
        //given
        book1.setId(1L);
        user1.setId(2L);
        given(bookService.getBookById(any())).willReturn(book1);
        given(appUserService.getAppUserById(any())).willReturn(user1);
        given(appUserService.getAppUserByUsername(any())).willReturn(user1);
        given(borrowedBooksRepository.findUserUnreturnedBooks(any(), any())).willReturn(List.of());
        Integer amountAvailable = book1.getAmountAvailable();
        //when
        BorrowedBooks borrowedBooks = underTest.borrowTheBook(book1.getId(), user1.getId());
        //then
        assertEquals(amountAvailable-1, book1.getAmountAvailable());
        assertEquals(user1, borrowedBooks.getAppUser());
        assertEquals(book1, borrowedBooks.getBook());
    }

    @Test
    void extendBorrowingPeriod_ifBookNotRenewable() {
        //given
        Optional<BorrowedBooks> mock = Optional.of(mock(BorrowedBooks.class));
        mock.get().setRenewable(0);
        given(bookService.getBookById(any())).willReturn(book1);
        given(appUserService.getAppUserById(any())).willReturn(user1);
        given(borrowedBooksRepository.getBorrowedBook(any(),any())).willReturn(mock);
        //when
        //then
        assertThrows(BookExtensionException.class, ()-> underTest.extendBorrowingPeriod(book1.getId(), user1.getId()));
    }

    @Test
    void extendBorrowingPeriod_ifValid() {
        //given
        BorrowedBooks inputBorrowedBooks = new BorrowedBooks(new BorrowedBooksId(user1.getId(), book1.getId()),user1, book1);
        given(bookService.getBookById(any())).willReturn(book1);
        given(appUserService.getAppUserById(any())).willReturn(user1);
        given(borrowedBooksRepository.getBorrowedBook(any(),any())).willReturn(Optional.of(inputBorrowedBooks));
        //when
        BorrowedBooks borrowedBooks = underTest.extendBorrowingPeriod(book1.getId(), user1.getId());
        //then
        assertNotNull(borrowedBooks);
        assertEquals(1, borrowedBooks.getRenewable());
        assertEquals(LocalDate.now().plusDays(book1.getBorrowingPeriod()), borrowedBooks.getEndDate());
    }

    @Test
    void returnTheBook() {
        //given
        BorrowedBooks inputBorrowedBooks = new BorrowedBooks(new BorrowedBooksId(user1.getId(), book1.getId()), user1, book1);
        Integer amountAvailable = book1.getAmountAvailable();
        given(bookService.getBookById(any())).willReturn(book1);
        given(appUserService.getAppUserById(any())).willReturn(user1);
        given(borrowedBooksRepository.getBorrowedBook(any(),any())).willReturn(Optional.of(inputBorrowedBooks));
        //when
        BorrowedBooks returnTheBook = underTest.returnTheBook(book1.getId(), user1.getId());
        //then
        assertNotNull(returnTheBook);
        assertNotNull(returnTheBook.getReturnedDate());
        assertEquals(0, returnTheBook.getRenewable());
        assertEquals(LocalDate.now(), returnTheBook.getReturnedDate());
        assertEquals(amountAvailable+1, returnTheBook.getBook().getAmountAvailable());
    }
}
