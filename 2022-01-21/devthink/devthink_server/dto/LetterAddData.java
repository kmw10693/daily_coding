package com.devthink.devthink_server.dto;

import com.github.dozermapper.core.Mapping;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LetterAddData {

    @Mapping("roomId")
    private Long roomId;

    @Mapping("sendId")
    private Long sendId;  // 메시지 보낸 사람

    @Mapping("recvId")
    private Long recvId; // 메시지 받는 사람

    @NotBlank(message = "쪽지의 내용을 입력해주세요.")
    @Mapping("content")
    private String content;


}
