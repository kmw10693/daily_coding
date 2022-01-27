package com.devthink.devthink_server.application;

import com.devthink.devthink_server.domain.Comment;
import com.devthink.devthink_server.domain.CommentRepository;
import com.devthink.devthink_server.domain.Review;
import com.devthink.devthink_server.domain.User;
import com.devthink.devthink_server.dto.CommentRequestDto;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    /**
     * 모든 Comment를 조회합니다.
     * @return
     */
    public List<Comment> getComments() {
        return commentRepository.findAll();
    }

    /**
     * 특정 Comment를 조회합니다.
     * @return 조회된 Comment 또는 {@literal Optional#empty()}
     */
    public Optional<Comment> getComment(Long commentId) {
        return commentRepository.findById(commentId);
    }

    /**
     * 입력된 comment 정보로 새로운 Comment를 생성합니다.
     * @param user Comment를 생성하려고 하는 User
     * @param review Comment가 달리게 되는 Review
     * @param commentRequestDto
     * @return 생성된 Comment의 id 값
     */
    public String createComment(User user, Review review, CommentRequestDto commentRequestDto) {
        Comment comment = commentRepository.save(
                Comment.builder()
                        .user(user)
                        .review(review)
                        .content(commentRequestDto.getContent())
                        .build()
        );
        return comment.getId().toString();
    }

    /**
     * commentId를 통하여 기존의 Comment를 수정합니다.
     * @param comment 수정할 Comment
     * @param content 수정할 content
     */
    public void updateComment(Comment comment, String content) {

        comment.setContent(content);
    }
}
