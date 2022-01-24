package com.devthink.devthink_server.dto;

import com.github.dozermapper.core.Mapping;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostModificationData {

    @NotBlank(message = "수정할 제목을 입력해주세요.")
    @Mapping("title")
    private String title;

    @NotBlank(message = "수정할 글의 내용을 입력해주세요.")
    @Mapping("content")
    private String content;

}
