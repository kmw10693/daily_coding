package com.devthink.devthink_server.infra;

import com.devthink.devthink_server.domain.UserRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRoomRepository extends JpaRepository<UserRoom, Long> {

    @Query("select u from UserRoom u where u.user.id = :userId and u.roomId = :roomId")
    Optional<UserRoom> getUserRoom(Long userId, Long roomId);

    // 메시지 테이블의 방번호 최댓값 가져오기
    @Query("select max(u.roomId) from UserRoom u")
    Long maxRoom();
}
