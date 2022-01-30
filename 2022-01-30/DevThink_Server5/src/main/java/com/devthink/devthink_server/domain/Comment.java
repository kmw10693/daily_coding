package com.devthink.devthink_server.domain;

import com.devthink.devthink_server.dto.CommentResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue
    Long id;

    @ManyToOne(fetch = LAZY)
    User user;

    //Post post;

    @ManyToOne(fetch = LAZY)
    Review review;

    String content;

    String status;

    public CommentResponseDto toCommentResponseDto() {
        return CommentResponseDto.builder()
                .user_nickname(user.getNickname())
                .user_role(user.getRole())
                .content(content)
                .build();
    }

    public void setContent(String content) {
        this.content = content;
    }

}