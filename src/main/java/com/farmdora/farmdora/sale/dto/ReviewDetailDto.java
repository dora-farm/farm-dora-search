package com.farmdora.farmdora.sale.dto;

import com.farmdora.farmdora.entity.Review;
import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDetailDto {
    private Integer reviewId;
    private String writer;
    private String content;
    private Byte score;
    private LocalDateTime createdDate;

    public static ReviewDetailDto fromEntity(Review review) {
        return ReviewDetailDto.builder()
                .reviewId(review.getId())
                .writer(review.getOrder().getUser().getName())
                .content(review.getContent())
                .score(review.getScore())
                .createdDate(review.getCreatedDate())
                .build();
    }
}
