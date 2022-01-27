package com.devthink.devthink_server.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Builder
@AllArgsConstructor
public class UserResultData {
    private Long id;

    private String email;

    private String name;

    private String nickname;

    private String phoneNum;

    private String role;

    private List<String> stack;

    private String blogAddr;

    private String gitNickname;

    private Integer point;

    private boolean deleted;

    private LocalDateTime create_at;

    private LocalDateTime update_at;
}
