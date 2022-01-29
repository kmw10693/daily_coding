package com.devthink.devthink_server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CommentResponseDto {

    @NotBlank
    private final String user_nickname;

    @NotBlank
    private final String user_role;

    @NotBlank
    private final String content;

}
