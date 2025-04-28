package com.farmdora.farmdora.sale.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
public class SaleSummaryDto {
    private Integer saleId;
    private String title;
    private Integer minPrice;
    private boolean isLiked;
    private String mainImage;

    @QueryProjection
    public SaleSummaryDto(Integer saleId, String title, Integer minPrice, boolean isLiked, String mainImage) {
        this.saleId = saleId;
        this.title = title;
        this.minPrice = minPrice;
        this.isLiked = isLiked;
        this.mainImage = mainImage;
    }
}
