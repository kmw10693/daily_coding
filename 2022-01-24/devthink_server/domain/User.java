package com.devthink.devthink_server.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "user")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue
    Long id;

    String email;

    String password;

    String nickName;

    String phoneNum;

    String stack;

    String blog_addr;

    String git_nickname;


}
