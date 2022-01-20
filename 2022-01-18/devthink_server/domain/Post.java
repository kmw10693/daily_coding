package com.devthink.devthink_server.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseTimeEntity{

    @Id
    @GeneratedValue
    private Long id;

    private Long user_id;

    private Long category_id;

    private String title;

    private String content;

    private int hit;

    private String status;

    public void update(String title, String content){
        this.title = title;
        this.content = content;
    }

}
