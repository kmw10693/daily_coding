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
public class LetterListData {

    private Long roomId;    // 방 번호

    private Long otherId;  // 상대방 아이디

    private String content; // 최근 메시지

    private LocalDateTime create_at; // 메시지 보낸 시각

    private Long unread; // 안 읽은 메시지 수

}
