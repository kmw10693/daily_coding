package com.devthink.devthink_server.infra;

import com.devthink.devthink_server.domain.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByTitleContaining(String keyword);

    Post save(Post post);

    @Query("select p from Post p where (p.createAt between :start and :end) and p.category.id = :category " +
            "order by p.heart desc")
    List<Post> getBestPost(Long category, LocalDateTime start, LocalDateTime end, Pageable pageable);
}
