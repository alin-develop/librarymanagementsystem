package com.lms.lms;

import com.lms.lms.appuser.AppUser;
import com.lms.lms.appuser.AppUserRole;
import com.lms.lms.appuser.AppUserService;
import com.lms.lms.book.Book;
import com.lms.lms.book.BookService;
import com.lms.lms.borrowedbooks.BorrowedBooksService;
import com.lms.lms.category.Category;
import com.lms.lms.category.CategoryService;
import com.lms.lms.security.jwt.JwtUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class LibraryManagementSystemApplication{

	public static void main(String[] args) {
		SpringApplication.run(LibraryManagementSystemApplication.class, args);
	}

	@Bean
	CommandLineRunner run(AppUserService appuserService, BookService bookService,
						  CategoryService categoryService, JwtUtil jwtUtil, BorrowedBooksService borrowedBooksService) {
		return args -> {

//			AppUser wanda = new AppUser("Wanda", "Maximoff", "wandamaximoff@gmail.com",
//					"maximoff2022", AppUserRole.USER);
//			appuserService.signUpUser(wanda);
//			appuserService.enableAppUser("wandamaximoff@gmail.com");
//			appuserService.signUpUser(new AppUser("Daenerys", "Targaryen", "daenerystargaryen@gmail.com",
//					"targaryen2022", AppUserRole.USER));
//			AppUser pietro = new AppUser("Pietro", "Maximoff", "pietromaximoff@gmail.com",
//					"maximoff2022", AppUserRole.LIBRARIAN);
//			appuserService.signUpUser(pietro);
//			appuserService.enableAppUser("pietromaximoff@gmail.com");
//			String pietromaximoff = jwtUtil.generateToken("pietromaximoff@gmail.com", AppUserRole.LIBRARIAN.getGrantedAuthorities());
//			System.out.println(pietromaximoff);
//			appuserService.signUpUser(new AppUser("Jane", "Doe", "janedoe@gmail.com",
//					"doe2022", AppUserRole.LIBRARIAN));
//			appuserService.enableAppUser("janedoe@gmail.com");
//
//			categoryService.addCategory(new Category("All Categories", null));
//			Category categoryByName = categoryService.findCategoryByName("All Categories").get(0);
//			categoryService.addCategory(new Category("Crime Fiction", categoryByName));
//
//			Book bookByIsbn = bookService.getBookByIsbn("9780136019702");
//			AppUser appUserByUsername = appuserService.getAppUserByUsername("janedoe@gmail.com");
//			borrowedBooksService.borrowTheBook(bookByIsbn.getId(), appUserByUsername.getId());
//
//			borrowedBooksService.extendBorrowingPeriod(bookByIsbn.getId(), appUserByUsername.getId());
//			borrowedBooksService.returnTheBook(bookByIsbn.getId(), appUserByUsername.getId());
//
//			Category crime_fiction = categoryService.findCategoryByName("Crime Fiction").get(0);
//			bookService.addCategory(bookByIsbn.getId(), crime_fiction.getId());
		};
	}
}
