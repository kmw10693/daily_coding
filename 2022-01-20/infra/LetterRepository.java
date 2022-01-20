package com.devthink.devthink_server.infra;

import com.devthink.devthink_server.domain.Letter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface LetterRepository extends JpaRepository<Letter, Long> {

   @Modifying
   @Query("select u from Letter u " +
           "where (u.recvId = :userId or u.sendId = :userId) and u.roomId = :roomId " +
           "order by u.id DESC")
   List<Letter> findView(Long userId, Long roomId);
}
