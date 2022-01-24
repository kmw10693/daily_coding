package com.devthink.devthink_server.domain;

import lombok.*;

import javax.persistence.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Letter extends BaseTimeEntity{

    @Id
    @GeneratedValue
    private Long id;

    private Long roomId;

    private Long senderId;  // 발신자

    private Long targetId;  // 수신자

    private String content;

    private LocalDateTime view_at; // 열람일자

    private Integer readCheck;

    private Long unread; // 안 읽은 메시지 갯수

    private Long other_id; // 현재 사용자의 메시지 상대 id를 담는다.

    public void change(Long unread){
        this.unread = unread;
    }

    public void changeOtherId(Long other_id)
    {
        this.other_id = other_id;
    }

}