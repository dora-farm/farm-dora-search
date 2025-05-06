package com.farmdora.farmdora.sale.dto;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SaleRankingDto {
    private Integer saleId;
    private String title;
    private Integer minPrice;
    private Long orderCount;
    private String imageUrl;
    private boolean isLiked;

    public SaleRankingDto(Integer saleId, String title, Integer minPrice, Long orderCount, String imageUrl) {
        this.saleId = saleId;
        this.title = title;
        this.minPrice = minPrice;
        this.orderCount = orderCount;
        this.imageUrl = imageUrl;
    }
}
