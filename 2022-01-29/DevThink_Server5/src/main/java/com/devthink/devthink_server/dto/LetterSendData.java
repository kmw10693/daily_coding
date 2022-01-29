package com.devthink.devthink_server.dto;

import com.github.dozermapper.core.Mapping;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Builder
@Getter
@AllArgsConstructor
public class LetterSendData {
    @Mapping("roomId")
    private Long roomId;

    @Mapping("senderId")
    private Long senderId;  // 쪽지 보낸 사람

    @Mapping("targetId")
    private Long targetId; // 쪽지 받은 사람

    @NotBlank(message = "쪽지의 내용을 입력해주세요.")
    @Mapping("content")
    private String content;

    @Mapping("heart")
    private Boolean heart;

    public void changeRoomId(Long roomId)
    {
        this.roomId = roomId;
    }
}
