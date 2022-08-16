package com.lms.lms.photo;

import com.lms.lms.book.Book;
import com.lms.lms.book.BookService;
import com.lms.lms.error.BookNotFoundException;
import com.lms.lms.error.PhotoNotFoundException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class PhotoService {

    private final PhotoRepository photoRepository;
    private final BookService bookService;

    @Autowired
    public PhotoService(PhotoRepository photoRepository, BookService bookService) {
        this.photoRepository = photoRepository;
        this.bookService = bookService;
    }

    public Photo getPhotoByBookId(@NonNull Long bookId) {
        Book book = bookService.getBookById(bookId);
        return photoRepository.findById(book.getPhoto().getId()).orElseThrow(() ->
                new PhotoNotFoundException(book.getPhoto().getId()));
    }


    public Photo getPhotoById(Long photoId) {
        return photoRepository.findById(photoId).orElseThrow(()-> new PhotoNotFoundException(photoId));
    }

    @Transactional
    public Photo addPhoto(Photo photo, Long bookId) {
        log.info("Start of the new photo.");

        Book book = bookService.getBookById(bookId);
        if (book == null || book.getId() == null) {
            assert book != null;
            throw new BookNotFoundException(book.getIsbn());
        }
        //update photo attributes assigned to the book
        book.getPhoto().setUrl_large(photo.getUrl_large());
        book.getPhoto().setUrl_medium(photo.getUrl_medium());
        book.getPhoto().setUrl_small(photo.getUrl_small());
        book.getPhoto().setBook(book);

        log.info("The id of the book: " + book.getId());

        return book.getPhoto();
    }

    @Transactional
    public void deletePhoto(@NonNull Long id) {
        photoRepository.findById(id).orElseThrow( () -> new PhotoNotFoundException(id) );

        photoRepository.deleteById(id);
    }
}
