package com.devthink.devthink_server.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Book extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer isbn;

    @Builder.Default
    private Integer reviewCnt = 0;

    @Builder.Default
    private BigDecimal scoreAvg = new BigDecimal("0");

    @Builder.Default
    @OneToMany(mappedBy = "book")
    private List<Review> reviews = new ArrayList<>();

    public void setScoreAvg(BigDecimal scoreAvg){
        this.scoreAvg = scoreAvg;
    }

    public void addReview(Review review) {
        review.setBook(this);
        reviews.add(review);
        upReviewCnt();
    }

    public void upReviewCnt(){
        reviewCnt++;
    }

    public void downReviewCnt(){
        reviewCnt--;
    }

}
