package com.devthink.devthink_server.dto;

import com.github.dozermapper.core.Mapping;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostRequestData {

    @Mapping("userId")
    @ApiModelProperty(value = "유저 아이디", example = "1")
    @ApiParam(value = "유저 아이디", example = "1")
    private Long userId;

    @Mapping("categoryId")
    @ApiModelProperty(value = "카테고리 아이디", example = "1")
    @ApiParam(value = "카테고리 아이디", example = "1")
    private Long categoryId;

    @NotBlank(message = "제목을 입력해주세요.")
    @ApiModelProperty(value = "제목", example = "example")
    @ApiParam(value = "제목", example = "example")
    @Mapping("title")
    private String title;

    @NotBlank(message = "글의 내용을 입력해주세요.")
    @ApiModelProperty(value = "내용", example = "example1")
    @ApiParam(value = "내용", example = "example1")
    @Mapping("content")
    private String content;


}
