package com.lms.lms.borrowedbooks;

import com.lms.lms.appuser.AppUser;
import com.lms.lms.appuser.AppUserService;
import com.lms.lms.book.Book;
import com.lms.lms.book.BookService;
import com.lms.lms.error.BookExtensionException;
import com.lms.lms.error.BookNotAvailableException;
import com.lms.lms.error.BookNotBorrowedException;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class BorrowedBooksService {

    private BorrowedBooksRepository borrowedBooksRepository;
    private BookService bookService;
    private AppUserService appUserService;

    //get all the unreturned books after deadline
    public List<BorrowedBooks> getAllUnreturnedBooks(){
        return borrowedBooksRepository.findAllUnreturnedBooks(LocalDate.now());
    }

    //find whether an user has unreturned books after deadline
    public List<BorrowedBooks> getAppUserUnreturnedBooks(@NonNull String username){
        AppUser appUser = appUserService.getAppUserByUsername(username);
        return borrowedBooksRepository.findUserUnreturnedBooks(appUser.getId(), LocalDate.now());
    }

    public BorrowedBooks getBorrowedBook(Long bookId, Long appUserId){
        return borrowedBooksRepository.getBorrowedBook(bookId, appUserId).orElseThrow(()->
                new BookNotBorrowedException(bookId, appUserId));
    }

    public BorrowedBooks borrowTheBook(@NonNull Long bookId, @NonNull Long appUserId) {
        log.info("Start of the borrowTheBook method.");
        Book book = bookService.getBookById(bookId);
        AppUser appUser = appUserService.getAppUserById(appUserId);
        log.info("Book and AppUser found.");
        if ( (book.getAmountAvailable() < 1) || (book.getBorrowingPeriod() < 1) ) throw new BookNotAvailableException(bookId);

        List<BorrowedBooks> borrowedBooksByUser = this.getAppUserUnreturnedBooks(appUser.getUsername());
        if (!borrowedBooksByUser.isEmpty()) throw new BookNotAvailableException(bookId);
        log.info("Unreturned books by the user fetched.");

        BorrowedBooks newBorrowedBook = new BorrowedBooks(
                new BorrowedBooksId(appUserId, bookId, LocalDate.now()), appUser, book);

        book.addBorrowedBooks(newBorrowedBook);
        appUser.addBorrowedBooks(newBorrowedBook);

        book.setAmountAvailable(book.getAmountAvailable()-1);

        log.info("The amountAvailable variable updated.");
        log.info(newBorrowedBook.toString());

        borrowedBooksRepository.save(newBorrowedBook);
        return newBorrowedBook;
    }


    public BorrowedBooks extendBorrowingPeriod(Long bookId, Long appUserId) {
        Book book = bookService.getBookById(bookId);
        appUserService.getAppUserById(appUserId);
        BorrowedBooks borrowedBook = this.getBorrowedBook(bookId, appUserId);

        if (borrowedBook.getRenewable() > 0) {
            borrowedBook.setRenewable(borrowedBook.getRenewable()-1);
            borrowedBook.setEndDate(LocalDate.now().plusDays(book.getBorrowingPeriod()));
        }else if ( borrowedBook.getRenewable() == 0 ) throw new BookExtensionException();

        return borrowedBook;
    }

    public BorrowedBooks returnTheBook(Long bookId, Long appUserId) {
        Book book = bookService.getBookById(bookId);
        appUserService.getAppUserById(appUserId);
        BorrowedBooks borrowedBook = this.getBorrowedBook(bookId, appUserId);

        borrowedBook.setReturnedDate(LocalDate.now());
        borrowedBook.setRenewable(0);
        book.setAmountAvailable(book.getAmountAvailable()+1);

        return borrowedBook;
    }

    public List<BorrowedBooks> getAllBorrowedBooks() {
        return borrowedBooksRepository.findAll();
    }

    public List<BorrowedBooks> getAllBorrowedBooksByUserNow(Long appUserId) {
        return borrowedBooksRepository.getAllBorrowedBooksByUserNow(appUserId);
    }

    public List<BorrowedBooks> getAllBorrowedBooksByUser(Long appUserId) {
        return borrowedBooksRepository.getAllBorrowedBooksByUser(appUserId);
    }

    public List<BorrowedBooks> getAllBorrowedBooksByUserUsingUsername(String username) {
        AppUser appUser = appUserService.getAppUserByUsername(username);
        return borrowedBooksRepository.getAllBorrowedBooksByUser(appUser.getId());
    }
}
