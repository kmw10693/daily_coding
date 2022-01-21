package com.devthink.devthink_server.infra;

import com.devthink.devthink_server.domain.Letter;
import com.devthink.devthink_server.domain.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface LetterRepository extends JpaRepository<Letter, Long> {

    List<Letter> findByUserIdAndRoomIdOrderByIdDesc(Long userId, Long roomId);

    Letter findFirstByUserIdAndRoomIdOrderByIdDesc(Long userId, Long roomId);

    @Modifying
    @Query("select max(p.roomId) from Letter p where p.userId = :userId")
    Long getMaxRoomId(Long userId);
}
