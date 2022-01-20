package com.devthink.devthink_server.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseTimeEntity{
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    public void update(String name){
        this.name = name;
    }

}
