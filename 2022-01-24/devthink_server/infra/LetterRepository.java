package com.devthink.devthink_server.infra;

import com.devthink.devthink_server.domain.Letter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.List;

public interface LetterRepository extends JpaRepository<Letter, Long> {

    // 방 별 메시지 리스트 가져오기
    @Query("select u from Letter u where u.id " +
            "in (select max(p.id) from Letter p group by p.roomId) " +
            "and (u.senderId = :userId or u.targetId = :userId) order by u.id desc")
    ArrayList<Letter> getMessageList(Long userId);

    // 안 읽은 메시지 갯수 가져오기
    @Query("select count(u.id) from Letter u where u.targetId = :userId and u.readCheck = 0 and u.roomId = :roomId")
    Long countUnread(Long userId, Long roomId);

    // 메시지 내용 가져오기
    @Query("select u from Letter u where u.roomId = :roomId and " +
            "(u.senderId = :userId or u.targetId = :userId)")
    ArrayList<Letter> getRoomLists(Long userId, Long roomId);

    // 메시지 읽음 처리하기
    @Modifying
    @Query("update Letter u set u.readCheck = 1 where u.roomId = :roomId and " +
            "u.readCheck = 0 and u.targetId = :userId")
    int MessageReadCheck(Long userId, Long roomId);

    // 메시지 이력이 있는지 검색하고, 게시글 개수 반환하기
    @Query("select count(u.id) from Letter u " +
            "where (u.targetId = :targetId and u.senderId = :senderId) or (u.senderId = :targetId and u.targetId = :senderId)")
    int existChat(Long targetId, Long senderId);

    // 유저의 메시지 테이블의 방 번호 최댓값 가져오기
    @Query("select max(u.roomId) from Letter u")
    Long maxRoom();

    // 기존 메시지 내역의 방 번호를 가져옵니다.
    @Query("select u from Letter u where (u.targetId = :targetId " +
            "and u.senderId = :senderId) or (u.senderId = :targetId and u.targetId = :senderId)")
    List<Letter> selectRoom(Long targetId, Long senderId, Pageable pageable);
}
