package com.devthink.devthink_server.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Long> {
    List<Comment> findAll();
    Optional<Comment> findById(Long id);
    Comment save(Comment comment);
}
