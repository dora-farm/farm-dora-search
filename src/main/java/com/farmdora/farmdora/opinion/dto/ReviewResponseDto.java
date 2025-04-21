package com.farmdora.farmdora.opinion.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
public class ReviewResponseDto {
    private Integer reviewId;
    private String saleTitle;
    private String reviewContent;
    private String writer;
    private LocalDateTime createdDate;
    private byte score;

    @QueryProjection
    public ReviewResponseDto(Integer reviewId,
                             String saleTitle,
                             String reviewContent,
                             String writer,
                             LocalDateTime createdDate,
                             byte score) {
        this.reviewId = reviewId;
        this.saleTitle = saleTitle;
        this.reviewContent = reviewContent;
        this.writer = writer;
        this.createdDate = createdDate;
        this.score = score;
    }
}
