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
public class CategoryDto {

    @Mapping("id")
    @ApiParam(value = "카테고리 고유 번호", example = "1")
    @ApiModelProperty(notes = "카테고리 고유 번호", example = "1")
    private Long id;

    @Mapping("name")
    @ApiParam(value = "카테고리 이름", example = "example", required = true)
    @ApiModelProperty(notes = "카테고리 이름", example = "example")
    @NotBlank(message = "카테고리 이름을 입력해주세요.")
    private String name;

}
