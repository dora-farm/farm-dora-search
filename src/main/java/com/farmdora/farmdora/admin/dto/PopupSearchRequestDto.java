package com.farmdora.farmdora.admin.dto;

import com.farmdora.farmdora.order.dto.Sort;
import java.time.LocalDate;
import java.util.List;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PopupSearchRequestDto {
    private String keyword;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<Short> types;
    private Sort sort;
}
