package com.devthink.devthink_server.domain;

import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserRoom extends BaseTimeEntity {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = LAZY)
    private User user1;

    @ManyToOne(fetch = LAZY)
    private User user2;

    private Long roomId;

    @Builder.Default
    private Boolean deleted = false;
}