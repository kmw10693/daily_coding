package com.devthink.devthink_server.infra;

import com.devthink.devthink_server.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByTitleContaining(String keyword);

    // 좋아요 기능 추가
    //Optional<Post> findCreateAtBetween(LocalDateTime start, LocalDateTime end);
}
