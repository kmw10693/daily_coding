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

    @Builder.Default
    private Long roomId = 0L;

    private Long userId;

    private String content;

    private LocalDateTime view_at; // 메시지 읽은 시간

    @Builder.Default
    private Integer read_chk = 0; // 읽었으면 1, 안읽었으면 0


}