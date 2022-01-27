package com.devthink.devthink_server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseData {

    private Long id;

    private Long user_id;

    private Long category_id;

    private String title;

    private String content;

    private String status;

    private Integer like;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;

}
