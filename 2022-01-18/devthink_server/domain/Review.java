package com.devthink.devthink_server.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Review {
    @Id
    @GeneratedValue
    Long id;

    @ManyToOne(fetch = LAZY)
    User user;

    @ManyToOne(fetch = LAZY)
    Book book;

    @OneToMany
    List<Comment> comments = new ArrayList<>();

    String content;

    Float Score;

    String status;
}
