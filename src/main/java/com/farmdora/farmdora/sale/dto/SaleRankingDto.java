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
}
