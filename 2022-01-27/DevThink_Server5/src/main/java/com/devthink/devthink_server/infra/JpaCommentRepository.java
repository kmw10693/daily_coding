package com.devthink.devthink_server.infra;

import com.devthink.devthink_server.domain.Comment;
import com.devthink.devthink_server.domain.CommentRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Primary
public interface JpaCommentRepository
        extends CommentRepository, CrudRepository<Comment, Long> {
    List<Comment> findAll();
    Comment save(Comment comment);
}
