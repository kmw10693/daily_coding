package com.devthink.devthink_server.infra;

import com.devthink.devthink_server.domain.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByTitleContaining(String keyword, Pageable pageable);

    @Modifying
    @Query("update Post p set p.hit = p.hit + 1 where p.id = :id")
    int updateView(Long id);
}
