package com.lms.lms.book;

import com.lms.lms.category.Category;
import com.lms.lms.category.CategoryService;
import com.lms.lms.error.BookAlreadyExistsException;
import com.lms.lms.error.BookDeletionException;
import com.lms.lms.error.BookNotFoundException;
import com.lms.lms.photo.Photo;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@Transactional
public class BookService {
    private final BookRepository bookRepository;
    private final CategoryService categoryService;

    @Autowired
    public BookService(BookRepository bookRepository, CategoryService categoryService) {
        this.bookRepository = bookRepository;
        this.categoryService = categoryService;
    }


    public List<Book> getBooks() {
        return bookRepository.findAll();
    }

    public Book addBook(@NotNull BookRequest book) {
        log.info("Start of the addBook method.");
        boolean bookExists = bookRepository.findBookByIsbn(book.getIsbn()).isPresent();

        if (bookExists) throw new BookAlreadyExistsException(book.getIsbn());

        if( book.getTotalAmount() < 0 || book.getName().isEmpty() || book.getBorrowingPeriod() < 0 )
            throw new IllegalStateException("The parameters for a new book are false.");

        Book new_book = new Book(book.getIsbn(), book.getName(), book.getAuthor(), book.getYearPublished(), book.getTotalAmount(),
                book.getBorrowingPeriod());
        new_book.setPhoto(new Photo());

        return bookRepository.save(new_book);
    }

    public Book addCategory(Long bookId, Long categoryId){
        Book bookById = getBookById(bookId);
        Category categoryById = categoryService.findCategoryById(categoryId);

        bookById.getCategories().add(categoryById);

        return bookById;
    }


    public Book getBookById(@NonNull Long id) {
        return bookRepository.findById(id).orElseThrow(()-> new BookNotFoundException(id));
    }

    public Book getBookByIsbn(@NonNull String isbn) {
        return bookRepository.findBookByIsbn(isbn).orElseThrow(()-> new BookNotFoundException(isbn));
    }


    public Book updateBook(Long id, String name, String author,
                           Integer yearPublished, Integer totalAmount, Integer amountAvailable) {
        Book oldBook = getBookById(id);

        if (name!=null && name.length()>0 && !Objects.equals(name, oldBook.getName())){
            oldBook.setName(name);
        }

        if (author!=null && author.length()>0 && !Objects.equals(author, oldBook.getAuthor())){
            oldBook.setAuthor(author);
        }

        if (yearPublished!=null && yearPublished>0 && !Objects.equals(yearPublished, oldBook.getYearPublished())){
            oldBook.setYearPublished(yearPublished);
        }

        if (totalAmount!=null && totalAmount>=0 && !Objects.equals(totalAmount, oldBook.getTotalAmount())){
            oldBook.setTotalAmount(totalAmount);
        }

        if (amountAvailable!=null && amountAvailable>=0 && !Objects.equals(amountAvailable, oldBook.getAmountAvailable())){
            oldBook.setAmountAvailable(amountAvailable);
        }

        return oldBook;
    }

    public Book updateBookByIsbn(@NotNull BookRequest bookRequest) {
        Book oldBook = bookRepository.findBookByIsbn(bookRequest.isbn).orElseThrow(() -> new BookNotFoundException(bookRequest.isbn));

        if (bookRequest.name!=null && bookRequest.name.length()>0 && !Objects.equals(bookRequest.name, oldBook.getName())){
            oldBook.setName(bookRequest.name);
        }

        if (bookRequest.author!=null && bookRequest.author.length()>0 && !Objects.equals(bookRequest.author, oldBook.getAuthor())){
            oldBook.setAuthor(bookRequest.author);
        }

        if (bookRequest.yearPublished>0 && !Objects.equals(bookRequest.yearPublished, oldBook.getYearPublished())){
            oldBook.setYearPublished(bookRequest.yearPublished);
        }

        if (bookRequest.totalAmount>=0 && !Objects.equals(bookRequest.totalAmount, oldBook.getTotalAmount())){
            oldBook.setTotalAmount(bookRequest.totalAmount);
        }

        if (bookRequest.amountAvailable>=0
                && !Objects.equals(bookRequest.amountAvailable, oldBook.getAmountAvailable())){
            oldBook.setAmountAvailable(bookRequest.amountAvailable);
        }
        return oldBook;
    }


    public boolean deleteBook(Long id) {
        log.info("Start of the method delete book.");
        getBookById(id);
        bookRepository.deleteById(id);
        if (bookRepository.findById(id).isPresent()) {
            throw new BookDeletionException(id);
        }
        else return true;
    }

}
