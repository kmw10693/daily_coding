package com.devthink.devthink_server.domain;

import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseTimeEntity{

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "CATEGORY_ID")
    private Category category;

    private String title;

    private String content;

    private String status;

    //ToDO 좋아요 기능 추가 할것
    @Builder.Default
    private Integer like =0;

    public void update(String title, String content){
        this.title = title;
        this.content = content;
    }

}
