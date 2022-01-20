package com.devthink.devthink_server.dto;

import com.github.dozermapper.core.Mapping;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {

    @Mapping("id")
    private Long id;

    @Mapping("name")
    @NotBlank(message = "카테고리 이름을 입력해주세요.")
    private String name;

}
