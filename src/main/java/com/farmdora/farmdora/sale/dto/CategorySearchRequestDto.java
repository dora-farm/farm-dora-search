package com.farmdora.farmdora.sale.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CategorySearchRequestDto {
    private String keyword;
    private Short bigTypeId;
    private Short typeId;
    private SaleSortType sort;
}
