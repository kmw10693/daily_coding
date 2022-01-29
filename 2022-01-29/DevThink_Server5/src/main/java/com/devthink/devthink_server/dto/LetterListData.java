package com.devthink.devthink_server.dto;

import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty(notes = "방 번호", example = "1")
    private Long roomId;    // 방 번호

    @ApiModelProperty(notes = "상대방 아이디", example = "1")
    private Long otherId;  // 상대방 아이디

    @ApiModelProperty(notes = "최근 메시지", example = "example")
    private String content; // 최근 메시지

    @ApiModelProperty(notes = "메시지 보낸 시각", example = "2022-01-26T22:07:17.0831141")
    private LocalDateTime createAt; // 메시지 보낸 시각

    @ApiModelProperty(notes = "안 읽은 메시지 갯수", example = "1")
    private Long unread; // 안 읽은 메시지 수

    @ApiModelProperty(notes = "상대방 프로필 이미지", example = "example")
    private String image; // 상대방 이미지;
}
