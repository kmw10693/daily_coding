package com.devthink.devthink_server.infra;

import com.devthink.devthink_server.domain.Letter;
import com.devthink.devthink_server.domain.UserRoom;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface UserRoomRepository extends JpaRepository<UserRoom, Long> {

    @Query("select u from UserRoom u where ((u.user1.id = :targetId and u.user2.id = :senderId)" +
            "or (u.user2.id = :targetId and u.user1.id = :senderId)) and u.roomId = :roomId")
    Optional<UserRoom> getUserRoom(Long targetId, Long senderId, Long roomId);

    // 메시지 테이블의 방번호 최댓값 가져오기
    @Query("select max(u.roomId) from UserRoom u")
    Long maxRoom();

    @Query("select count(u.id) from UserRoom u " +
            "where (u.user1.id = :senderId and u.user2.id = :targetId) or (u.user2.id = :senderId and u.user1.id = :targetId)")
    int existChat(Long senderId, Long targetId);

    @Query("select u from UserRoom u where (u.user1.id = :targetId " +
            "and u.user2.id = :senderId) or (u.user2.id = :targetId and u.user1.id = :senderId)")
    List<UserRoom> selectRoom(Long targetId, Long senderId, Pageable pageable);

    @Query("select u from UserRoom u where (u.user1.id = :userId or u.user2.id = :userId)")
    List<UserRoom> findRoomId(Long userId);

}
