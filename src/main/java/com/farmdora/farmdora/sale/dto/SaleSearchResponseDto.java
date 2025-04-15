package com.farmdora.farmdora.sale.dto;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SaleSearchResponseDto {
    private int saleId;
    private String title;
    private int price;
    private boolean isBlind;
    private int stock;
    private Long orderCount;
}
