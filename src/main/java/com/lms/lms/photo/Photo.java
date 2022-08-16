package com.lms.lms.photo;

import com.lms.lms.book.Book;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Photo {

    @Id
    @GeneratedValue
    private Long id;

    private String url_small;
    private String url_medium;
    private String url_large;

    @OneToOne(mappedBy = "photo")
    private Book book;

    public Photo(String url_small, String url_medium, String url_large) {
        this.url_small = url_small;
        this.url_medium = url_medium;
        this.url_large = url_large;
    }

    public Photo() {}
}
