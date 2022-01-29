package com.devthink.devthink_server.dto;


import com.devthink.devthink_server.domain.BaseTimeEntity;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ReviewResponseDto {

    private Long id;

    private Long userId;

    private Integer bookIsbn;

    private String content;

    private BigDecimal score;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;

}
