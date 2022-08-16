package com.lms.lms.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/book")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }


    /**
     * Get all books
     * Accessible by everyone
     * @return ResponseEntity of a list of all books
     * */
    @PreAuthorize("permitAll()")
    @GetMapping
    public ResponseEntity<List<BookResponse>> getBooks(){
        List<BookResponse> response = bookService.getBooks().stream().map(BookResponse::new).toList();
        return ResponseEntity.ok(response);
    }

    /**
     * Find book by the provided book id
     * @param id - the book id
     * @return ResponseEntity of a found book object
     * */
    @PreAuthorize("permitAll()")
    @GetMapping(path = "/find/{id}")
    public ResponseEntity<BookResponse> getBookById(@PathVariable("id") Long id){
        return new ResponseEntity<>(new BookResponse(bookService.getBookById(id)), HttpStatus.OK);
    }

    /**
     * Find book by the provided book isbn
     * @param isbn - the book isbn
     * @return ResponseEntity of a found book object
     * */
    @PreAuthorize("permitAll()")
    @GetMapping(path = "/find/isbn/{isbn}")
    public ResponseEntity<BookResponse> getBookByIsbn(@PathVariable("isbn") String isbn){
        return new ResponseEntity<>(new BookResponse(bookService.getBookByIsbn(isbn)), HttpStatus.OK);
    }

    /**
     * Add a book.
     * Accessible by the librarians only
     * @param book - passing a form request object of new book
     * @return ResponseEntity of a newly added book object
     * */

    @PostMapping(path = "/add")
    @PreAuthorize("hasAuthority('LIBRARIAN')")
    public ResponseEntity<BookResponse> addBook(@RequestBody BookRequest book){
        URI uri = URI.create(ServletUriComponentsBuilder
                .fromCurrentContextPath().path("api/v1/book/add").toUriString());
        return ResponseEntity.created(uri).body(new BookResponse(bookService.addBook(book)));
    }

    /**
     * Update the book by using the provided book id.
     * Accessible by the librarians only
     * @param id - the book id
     * @return ResponseEntity of Http Status OK with the updated book object
     * */

    @PutMapping(path = "/update/{id}")
    @PreAuthorize("hasAuthority('LIBRARIAN')")
    public ResponseEntity<BookResponse> updateBook(@PathVariable("id") Long id,
                                       @RequestParam(required = false) String name,
                                       @RequestParam(required = false) String author,
                                       @RequestParam(required = false) Integer yearPublished,
                                       @RequestParam(required = false) Integer totalAmount,
                                       @RequestParam(required = false) Integer amountAvailable){
        return new ResponseEntity<>(new BookResponse(bookService.updateBook
                (id, name, author, yearPublished, totalAmount, amountAvailable)), HttpStatus.OK);
    }

    /**
     * Update the book by using the provided book isbn.
     * Accessible by the librarians only
     * @param bookRequest - the book request form
     * @return ResponseEntity of Http Status OK with the updated book object
     * */

    @PutMapping(path = "/update/isbn/")
    @PreAuthorize("hasAuthority('LIBRARIAN')")
    public ResponseEntity<BookResponse> updateBookByIsbn(@RequestBody BookRequest bookRequest){
        return new ResponseEntity<>(new BookResponse(bookService.updateBookByIsbn
                (bookRequest)), HttpStatus.OK);
    }

    /**
     * Delete the book by the provided book id.
     * Accessible by the librarians only
     * @param id - the book id gotten from a path
     * @return ResponseEntity of Http Status OK if deletion was successful
     * otherwise returns Bad Request
     * */

    @DeleteMapping(path = "/delete/{id}")
    @PreAuthorize("hasAuthority('LIBRARIAN')")
    public ResponseEntity<?> deleteBook(@PathVariable("id") Long id){

        return (bookService.deleteBook(id))
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }

}
