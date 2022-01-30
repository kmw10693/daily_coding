package com.devthink.devthink_server.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
public class CommentRequestDto {

    @NotBlank
    private final Long user_id;

    //TODO: link Post by post_id
    //private final Long post_id;

    @NotBlank
    private final Long review_id;

    @NotBlank
    @Size(min=1)
    private final String content;

}
