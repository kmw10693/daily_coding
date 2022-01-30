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
    private UserRoom room;    // 방 번호

    @ManyToOne(fetch = LAZY)
    private User sender;  // 발신자

    @ManyToOne(fetch = LAZY)
    private User target;  // 수신자

    private String content; // 쪽지 내용

    private LocalDateTime viewAt; // 열람일자

    @Builder.Default
    private boolean readCheck = false;

    @Builder.Default
    private boolean heart = false;  // 하트 기능
}