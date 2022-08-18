package com.lms.lms.photo;

import com.lms.lms.book.Book;
import com.lms.lms.book.BookService;
import com.lms.lms.error.PhotoNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class PhotoServiceTest {

    @Mock private PhotoRepository photoRepository;
    @Mock private BookService bookService;

    private PhotoService underTest;

    Book book1 = new Book("9780136019701", "Chemistry: An Introduction to General, Organic, & Biological Chemistry (10th Edition)",
            "Timberlake, Karen C.", 2008, 3, 20);



    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        underTest = new PhotoService(photoRepository, bookService);
        book1.setPhoto(new Photo());
        book1.getPhoto().setId(1235L);
    }



    @Test
    void getPhotoByBookId_givenBookExists() {
        //given
        Optional<Photo> mock = Optional.of(mock(Photo.class));
        given(bookService.getBookById(any())).willReturn(book1);
        given(photoRepository.findById(any())).willReturn(mock);

        Photo photoByBookId = underTest.getPhotoByBookId(1L);

        assertNotNull(photoByBookId);
        assertEquals(mock.get(), photoByBookId);
    }

    @Test
    void getPhotoByBookId_givenBookExists_And_PhotoDoesNot() {

        given(bookService.getBookById(any())).willReturn(book1);
        given(photoRepository.findById(any())).willReturn(Optional.empty());

        assertThrows( PhotoNotFoundException.class, () -> underTest.getPhotoByBookId(2L) );
    }

    @Test
    void getPhotoById_givenPhotoExists() {
        given(photoRepository.findById(any())).willReturn(Optional.of(mock(Photo.class)));
        assertNotEquals(Optional.empty(), Optional.ofNullable(underTest.getPhotoById(book1.getPhoto().getId())));
    }

    @Test
    void getPhotoById_givenPhotoDoesNotExist() {
        Long photoIdDoesNotExist = 123456789L;
        given(photoRepository.findById(any())).willReturn(Optional.empty());
        assertThrows(PhotoNotFoundException.class, () -> underTest.getPhotoById(photoIdDoesNotExist));
    }

    @Test
    void addPhoto_ifValid() {
        Photo photo = new Photo("url_small_test", "url_medium_test", "url_large_test");

        given(bookService.getBookById(book1.getId())).willReturn(book1);
        Photo addPhoto = underTest.addPhoto(photo, book1.getId());

        assertNotNull(addPhoto);
        assertEquals(photo.getUrl_small(), addPhoto.getUrl_small());
        assertEquals(photo.getUrl_medium(), addPhoto.getUrl_medium());
        assertEquals(photo.getUrl_large(), addPhoto.getUrl_large());
    }

    @Test
    void deletePhoto_ifPhotoNotFound() {
        given(photoRepository.findById(any())).willReturn(Optional.empty());
        assertThrows(PhotoNotFoundException.class, () -> underTest.deletePhoto(5L) );
    }

    @Test
    void deletePhoto_ifValid() {
        given(photoRepository.findById(any())).willReturn(Optional.ofNullable(book1.getPhoto()));
        underTest.deletePhoto(book1.getPhoto().getId());

        then(photoRepository).should().deleteById(book1.getPhoto().getId());
    }

}