package com.devthink.devthink_server.application;
import com.devthink.devthink_server.domain.Book;
import com.devthink.devthink_server.domain.Review;
import com.devthink.devthink_server.domain.User;
import com.devthink.devthink_server.dto.ReviewRequestDto;
import com.devthink.devthink_server.errors.ReviewNotFoundException;
import com.devthink.devthink_server.infra.BookRepository;
import com.devthink.devthink_server.infra.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;

    // 리뷰 등록
    @Transactional
    public String createReview(User user, Book book, ReviewRequestDto reviewRequestDto){
        Review review = reviewRepository.save(
            Review.builder()
                    .user(user)
                    .book(book)
                    .content(reviewRequestDto.getContent())
                    .score(reviewRequestDto.getScore())
                    .build()
        );
        review.getBook().addReview(review);
        review.getBook().setScoreAvg(bookRepository.calcScoreAvg(book.getId()));
        return review.getId().toString();
    }

    //리뷰 조회
    public Review getReviewById(Long id) {
        return reviewRepository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new ReviewNotFoundException(id));
    }

    // 리뷰 내용 수정
    @Transactional
    public void updateContent(Review review, String content){
        review.setContent(content);
    }

    // 리뷰 점수 수정
    @Transactional
    public void updateScore(Review review, BigDecimal score){
        review.setScore(score);
    }

    // 리뷰 삭제
    @Transactional
    public void deleteReview(Review review){
        review.setDeleted(true);
        review.getBook().downReviewCnt();
        review.getBook().setScoreAvg(bookRepository.calcScoreAvg(review.getBook().getId()));
    }

}
