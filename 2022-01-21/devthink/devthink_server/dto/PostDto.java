package com.devthink.devthink_server.dto;

import com.github.dozermapper.core.Mapping;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {

    @Mapping("user_id")
    private Long user_id;

    @Mapping("category_id")
    private Long category_id;

    @NotBlank(message = "제목을 입력해주세요.")
    @Mapping("title")
    private String title;

    @NotBlank(message = "글의 내용을 입력해주세요.")
    @Mapping("content")
    private String content;

    @Mapping("status")
    @Builder.Default
    private String status = "active";


}
