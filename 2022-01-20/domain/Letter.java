package com.devthink.devthink_server.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.sql.Timestamp;
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

    private Long roomId;

    private Long sendId;    // 메시지 보낸 사람

    private Long recvId;    // 메시지 읽을 사람

    private String content;

    private LocalDateTime view_at; // 메시지 읽은 시간

    @Builder.Default
    private Integer read_chk = 0; // 읽었으면 1, 안읽었으면 0


}