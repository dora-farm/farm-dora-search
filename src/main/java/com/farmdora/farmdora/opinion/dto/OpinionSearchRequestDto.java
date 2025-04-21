package com.farmdora.farmdora.opinion.dto;

import com.farmdora.farmdora.order.dto.SearchPeriod;
import com.farmdora.farmdora.order.dto.SearchType;
import com.farmdora.farmdora.order.dto.Sort;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OpinionSearchRequestDto {
    private Integer sellerId;  // TODO JWT 구현완료 후 제거 예정
    private SearchType searchType;
    private String keyword;
    private SearchPeriod searchPeriod;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @Builder.Default
    private List<ProcessType> processTypes = new ArrayList<>();

    @Builder.Default
    private Sort sort = Sort.LATEST;
}
