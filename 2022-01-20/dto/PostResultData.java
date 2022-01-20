package com.devthink.devthink_server.dto;

import com.devthink.devthink_server.domain.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostResultData {
    private Long id;

    private Long user_id;

    private Long category_id;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @Builder.Default
    private Integer hit = 0;

    @Builder.Default
    private String status = "active";

    private LocalDateTime created_at;

    private LocalDateTime updated_at;

}
