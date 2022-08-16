package com.lms.lms.book;

import com.lms.lms.photo.Photo;
import com.lms.lms.photo.PhotoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class BookConfiguration {

    @Bean
    public CommandLineRunner commandLineRunnerBook(BookService bookService, PhotoService photoService){
        return args -> {
//            bookService.addBook(new BookRequest("9780140043129", "One Flew Over the Cuckoo's Nest", "Ken Kesey",
//                    1962, 1));
//            bookService.addBook(new BookRequest("9780136019701", "Chemistry: An Introduction to General, Organic, & Biological Chemistry (10th Edition)",
//                    "Timberlake, Karen C.", 2008, 3, 20));
//            bookService.addBook(new BookRequest("9780136019702","The World According to Garp", "John Irving",
//                    1978, 5, 28));
//            Book bookByIsbn = bookService.getBookByIsbn("9780140043129");
//            photoService.addPhoto(new Photo("url", "url", "url"), bookByIsbn.getId());

        };
    }
}
