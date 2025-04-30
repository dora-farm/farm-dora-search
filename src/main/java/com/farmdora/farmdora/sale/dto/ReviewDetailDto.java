package com.farmdora.farmdora.sale.dto;

import com.farmdora.farmdora.entity.Review;
import com.farmdora.farmdora.entity.ReviewFile;
import java.time.LocalDateTime;
import java.util.List;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDetailDto {
    private Integer reviewId;
    private String writer;
    private String content;
    private Byte score;
    private LocalDateTime createdDate;
    private List<String> reviewFiles;

    public static ReviewDetailDto fromEntity(Review review, List<ReviewFile> reviewFiles) {
        return ReviewDetailDto.builder()
                .reviewId(review.getId())
                .writer(review.getOrder().getUser().getName())
                .content(review.getContent())
                .score(review.getScore())
                .createdDate(review.getCreatedDate())
                .reviewFiles(reviewFiles.stream()
                        .map(ReviewFile::getSaveFile)
                        .toList())
                .build();
    }
}
