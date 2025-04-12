package com.farmdora.farmdora.sale.dto;

import com.farmdora.farmdora.order.dto.Sort;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SaleSearchRequestDto {
    private Integer sellerId;
    private String keyword;

    @Builder.Default
    private Sort sort = Sort.LATEST;

    @Builder.Default
    private Set<SaleStatus> filters = new HashSet<>();

    private Short typeBigId;
    private Short typeId;

    @Builder.Default
    private Integer page = 0;

    public Pageable toPageable() {
        final int size = 10;
        if (this.page != null) {
            return PageRequest.of(this.page, size);
        }
        return PageRequest.of(0, size);
    }
}
