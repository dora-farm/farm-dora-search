package com.farmdora.farmdora.sale.mapper;

import com.farmdora.farmdora.sale.dto.SaleSearchResponseDto;
import com.farmdora.farmdora.sale.dto.querydsl.SaleDto;
import com.farmdora.farmdora.sale.dto.querydsl.SaleOrderCountDto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class SaleMapper {

    public List<SaleSearchResponseDto> mapToSaleSearchResponseDto(List<Integer> saleIds, List<SaleDto> sales, List<SaleOrderCountDto> orderCounts) {
        Map<Integer, SaleDto> saleMap = new HashMap<>();
        for (SaleDto sale : sales) {
            saleMap.put(sale.getSaleId(), sale);
        }

        Map<Integer, Long> orderCountMap = new HashMap<>();
        for (SaleOrderCountDto count : orderCounts) {
            orderCountMap.put(count.getSaleId(), count.getOrderCount());
        }

        List<SaleSearchResponseDto> saleSearchResult = new ArrayList<>();
        for (Integer saleId : saleIds) {
            SaleSearchResponseDto dto = new SaleSearchResponseDto();

            if (saleMap.containsKey(saleId)) {
                SaleDto sale = saleMap.get(saleId);
                dto.setSaleId(sale.getSaleId());
                dto.setTitle(sale.getTitle());
                dto.setBlind(sale.isBlind());
                dto.setPrice(sale.getPrice());
                dto.setStock(sale.getStock());
            }

            Long orderCount = orderCountMap.getOrDefault(saleId, 0L);
            dto.setOrderCount(orderCount);

            saleSearchResult.add(dto);
        }

        return saleSearchResult;
    }

}
