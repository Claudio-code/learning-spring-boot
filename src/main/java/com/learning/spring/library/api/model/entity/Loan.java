package com.learning.spring.library.api.model.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "loans")
public class Loan implements Serializable {
    public static final String CACHE_NAME = "loan";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String customer;

    @OneToOne(cascade = CascadeType.ALL)
    private Book book;

    @Column
    private LocalDate loanDate;

    @Column
    private Boolean returned;
}
