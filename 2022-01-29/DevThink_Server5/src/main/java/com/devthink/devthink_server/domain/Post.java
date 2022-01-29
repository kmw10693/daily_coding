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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    private String title;

    private String content;

    @Builder.Default
    private String imageUrl = "";

    @Builder.Default
    private Boolean deleted = false;

    @Builder.Default
    private Integer heart = 0;

    public void update(String title, String content){
        this.title = title;
        this.content = content;
    }
}
