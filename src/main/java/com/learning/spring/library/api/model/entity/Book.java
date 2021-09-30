package com.learning.spring.library.api.model.entity;

import com.learning.spring.library.exception.InvalidBookException;
import lombok.*;
import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "books")
public class Book implements Serializable {

    public static final String CACHE_NAME = "book";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String author;

    @Column
    private String isbn;

    public void validFields() {
        if (isbn != null || author != null || title != null || id != null) {
            return;
        }
        throw new InvalidBookException();
    }

}
