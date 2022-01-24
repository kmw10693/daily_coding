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
public class LetterResultData {

    private Long roomId;

    private Long senderId;

    private Long targetId;

    private String content;

    private LocalDateTime create_at;

    private Integer readCheck;
}
