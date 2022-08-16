package com.lms.lms.borrowedbooks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/borrowedbooks")
public class BorrowedBooksController {
    private final BorrowedBooksService borrowedBooksService;
    @Autowired
    public BorrowedBooksController(BorrowedBooksService borrowedBooksService) {
        this.borrowedBooksService = borrowedBooksService;
    }

    /**
     * Get all borrowed books by the user that were not returned
     * Accessible by the librarians and users themselves
     * @param userId - the id of the user
     * @return ResponseEntity of List of borrowedBooks Response form
     * */

    @GetMapping(path = "/get/now/{userId}")
    @PreAuthorize("hasAuthority('LIBRARIAN')")
    public ResponseEntity<List<BorrowedBooksResponse>> getAllBorrowedBooksByUserNow(@PathVariable("userId") Long userId){

        ResponseEntity<List<BorrowedBooksResponse>> listResponseEntity = new ResponseEntity<>(borrowedBooksService
                .getAllBorrowedBooksByUserNow(userId).stream()
                .map(BorrowedBooksResponse::new).toList(), HttpStatus.OK);
        return listResponseEntity;

    }


    /**
     * Get all borrowed books by the user since the beginning till now
     * Accessible by the librarians and users themselves
     * @param appUserId - the id of the user
     * @return ResponseEntity of List of borrowedBooks Response form
     * */

    @GetMapping(path = "/get/{userId}")
    @PreAuthorize("hasAnyAuthority('LIBRARIAN')")
    public ResponseEntity<List<BorrowedBooksResponse>> getAllBorrowedBooksByUser(@PathVariable("userId") Long appUserId){

        List<BorrowedBooksResponse> borrowedBooksResponseList = borrowedBooksService.getAllBorrowedBooksByUser(appUserId)
                .stream().map(BorrowedBooksResponse::new).toList();

        return new ResponseEntity<>(borrowedBooksResponseList, HttpStatus.OK);
    }

    /**
     * Get all borrowed books by the user since the beginning till now
     * Accessible by the librarians and users themselves
     * @param username - the username/email of the user
     * @return ResponseEntity of List of borrowedBooks Response form
     * */

    @GetMapping(path = "/get/username")
    @PreAuthorize("hasAnyAuthority('LIBRARIAN') or principal.username=#username")
    public ResponseEntity<List<BorrowedBooksResponse>> getAllBorrowedBooksByUserUsingUsername( @RequestParam String username ){

        List<BorrowedBooksResponse> borrowedBooksResponseList = borrowedBooksService.getAllBorrowedBooksByUserUsingUsername(username)
                .stream().map(BorrowedBooksResponse::new).toList();

        return new ResponseEntity<>(borrowedBooksResponseList, HttpStatus.OK);
    }

    /**
     * Get all borrowed books since the beginning till now
     * Only accessible by librarians
     * @return ResponseEntity of List of borrowedBooks Response form
     * */

    @GetMapping
    @PreAuthorize("hasAuthority('LIBRARIAN')")
    public ResponseEntity<List<BorrowedBooksResponse>> getAllBorrowedBooks(){

        List<BorrowedBooksResponse> borrowedBooksResponseList = borrowedBooksService.getAllBorrowedBooks()
                .stream().map(BorrowedBooksResponse::new).toList();

        return new ResponseEntity<>(borrowedBooksResponseList, HttpStatus.OK);
    }

    /**
     * Get all unreturned books since the beginning till now
     * Only accessible by librarians
     * @return ResponseEntity of List of borrowedBooks Response form
     * */

    @GetMapping(path = "get/unreturnedbooks")
    @PreAuthorize("hasAuthority('LIBRARIAN')")
    public ResponseEntity<List<BorrowedBooksResponse>> getAllUnreturnedBooks(){

        List<BorrowedBooksResponse> borrowedBooksResponseList = borrowedBooksService.getAllUnreturnedBooks()
                .stream().map(BorrowedBooksResponse::new).toList();

        return new ResponseEntity<>( borrowedBooksResponseList, HttpStatus.OK );
    }

    /**
     * Allows user to borrow the book from the library
     * @param bookId - the id of the book
     * @param appUserId - the id of the app_user
     * @return ResponseEntity of a new borrowedBooks response form with provided bookId and provided appUserId
     * if successful
     * */
    @PostMapping(path = "/borrow")
    @PreAuthorize("hasAuthority('LIBRARIAN')")
    public ResponseEntity<BorrowedBooksResponse> borrowTheBook(@RequestParam Long bookId, @RequestParam Long appUserId){

        BorrowedBooksResponse borrowedBooksResponse = new BorrowedBooksResponse(
                borrowedBooksService.borrowTheBook(bookId, appUserId)
        );

        return new ResponseEntity<>( borrowedBooksResponse, HttpStatus.OK );
    }

    /**
    * Allows user or librarian extend the borrowing period of the already borrowed at the moment
     * by the user book up to 2 times
     * It can be done by either users or librarians
     * @param bookId - the id of the book
     * @param appUserId - the id of the app_user
     * @return ResponseEntity of borrowedBooks Response form with bookId and appUserId
     * */
    @PutMapping(path = "/extend")
    @PreAuthorize("hasAnyAuthority('USER','LIBRARIAN')")
    public ResponseEntity<BorrowedBooksResponse> extendBorrowingPeriod(@RequestParam Long bookId, @RequestParam Long appUserId){

        BorrowedBooksResponse borrowedBooksResponse = new BorrowedBooksResponse(borrowedBooksService
                .extendBorrowingPeriod(bookId, appUserId)
        );

        return new ResponseEntity<>( borrowedBooksResponse,HttpStatus.OK );
    }

    /**
     * Allows user to return the book to librarian who then updates the data in the app
     * It can be done by librarians
     * @param bookId - the id of the book
     * @param appUserId - the id of the app_user
     * @return ResponseEntity of borrowedBooks Response form with bookId and appUserId and new returned date
     * */
    @PutMapping(path = "/return")
    @PreAuthorize("hasAuthority('LIBRARIAN')")
    public ResponseEntity<BorrowedBooksResponse> returnTheBook(@RequestParam Long bookId, @RequestParam Long appUserId){

        BorrowedBooksResponse borrowedBooksResponse = new BorrowedBooksResponse(
                borrowedBooksService.returnTheBook(bookId, appUserId)
        );

        return new ResponseEntity<>( borrowedBooksResponse, HttpStatus.OK );
    }

}
