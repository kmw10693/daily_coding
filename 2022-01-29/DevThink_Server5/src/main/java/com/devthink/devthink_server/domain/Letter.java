package com.devthink.devthink_server.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Letter extends BaseTimeEntity{
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "userRoom_id")
    private UserRoom room;    // 방 번호

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User sender;  // 발신자

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User target;  // 수신자

    private String content; // 쪽지 내용

    private LocalDateTime viewAt; // 열람일자

    @Builder.Default
    private Integer readCheck = 0;  // 수신 확인

    @Builder.Default
    private boolean heart = false;  // 하트 기능
}