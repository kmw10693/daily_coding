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

    private Long room_id;

    private Long user_id;

    private String content;

    private LocalDateTime view_at;

    private Integer read_chk;

    // 보낸 시각
    private LocalDateTime create_at;
}
