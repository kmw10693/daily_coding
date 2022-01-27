package com.devthink.devthink_server.dto;

import lombok.*;
import org.apache.tomcat.jni.Local;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LetterModificationData {

    private Long id;

    private Long roomId;

    private Long senderId;  // 발신자

    private Long targetId;  // 수신자

    private String content;

    private LocalDateTime viewAt; // 열람일자

    private LocalDateTime createAt;

    private Integer readCheck;

    private Long unread; // 안 읽은 메시지 갯수

    private Long otherId; // 현재 사용자의 메시지 상대 id를 담는다.

    private String profile;

    public void change(Long unread){
        this.unread = unread;
    }

    public void changeOtherId(Long otherId)
    {
        this.otherId = otherId;
    }

}
