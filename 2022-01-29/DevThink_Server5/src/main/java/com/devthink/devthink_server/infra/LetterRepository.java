package com.devthink.devthink_server.infra;

import com.devthink.devthink_server.domain.Letter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public interface LetterRepository extends JpaRepository<Letter, Long> {

    // 방 별 메시지 리스트 가져오기
    @Query("select u from Letter u where u.id " +
            "in (select max(p.id) from Letter p group by p.room.id) " +
            "and (u.sender.id = :userId or u.target.id = :userId) order by u.id desc")
    ArrayList<Letter> getMessageList(Long userId);

    // 안읽은 메시지 개수 가져오기
    @Query("select count(u.id) from Letter u where u.target.id = :userId and u.readCheck = 0 and u.room.id = :roomId")
    Long countUnread(Long userId, Long roomId);

    // 메시지 내용 가져오기
    @Query("select u from Letter u where u.room.id = :roomId and " +
            "(u.sender.id = :userId or u.target.id = :userId)")
    ArrayList<Letter> getRoomLists(Long userId, Long roomId);

    // 메시지 읽음 처리하기
    @Modifying
    @Query("update Letter u set u.readCheck = 1, u.viewAt = :date where u.room.id = :roomId and " +
            "u.readCheck = 0 and u.target.id = :userId")
    int MessageReadCheck(Long userId, Long roomId, LocalDateTime date);

    // 메시지 이력 검색, 게시글 개수 반환하기
    @Query("select count(u.id) from Letter u " +
            "where (u.target.id = :targetId and u.sender.id = :senderId) or (u.sender.id = :targetId and u.target.id = :senderId)")
    int existChat(Long targetId, Long senderId);

    // 메시지 테이블의 방번호 최댓값 가져오기
    @Query("select max(u.room.id) from Letter u")
    Long maxRoom();

    // 메시지 내역의 방 번호를 가져오기
    @Query("select u from Letter u where (u.target.id = :targetId " +
            "and u.sender.id = :senderId) or (u.sender.id = :targetId and u.target.id = :senderId)")
    List<Letter> selectRoom(Long targetId, Long senderId, Pageable pageable);
}
