package com.farmdora.farmdora.sale.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SaleRelatedInfoDto {
    private Integer saleId;
    private String title;
    private Integer price;
    private Double score;
    private Long reviewCount;
}
