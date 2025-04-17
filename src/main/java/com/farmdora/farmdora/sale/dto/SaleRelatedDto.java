package com.farmdora.farmdora.sale.dto;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SaleRelatedDto {
    private Integer saleId;
    private String image;
    private String saleTitle;
    private Integer price;
    private Double score;
    private Long reviewCount;
    private boolean isLike;

    public static SaleRelatedDto createSaleRelatedDto(SaleRelatedInfoDto saleInfo, String mainImage, boolean isLike) {
        return SaleRelatedDto.builder()
                .saleId(saleInfo.getSaleId())
                .image(mainImage)
                .saleTitle(saleInfo.getTitle())
                .price(saleInfo.getPrice())
                .score(saleInfo.getScore())
                .reviewCount(saleInfo.getReviewCount())
                .isLike(isLike)
                .build();
    }
}
