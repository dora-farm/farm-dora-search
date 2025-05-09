package com.farmdora.farmdora.sale.service;

import com.farmdora.farmdora.common.response.PageResponseDto;
import com.farmdora.farmdora.sale.dto.SaleSearchRequestDto;
import com.farmdora.farmdora.sale.dto.SaleSearchResponseDto;
import com.farmdora.farmdora.sale.dto.querydsl.SaleDto;
import com.farmdora.farmdora.sale.dto.querydsl.SaleOrderCountDto;
import com.farmdora.farmdora.sale.mapper.SaleMapper;
import com.farmdora.farmdora.sale.repository.SaleRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SellerSaleService {

    private final SaleRepository saleRepository;
    private final SaleMapper saleMapper;

    @Transactional(readOnly = true)
    public PageResponseDto<SaleSearchResponseDto> searchSales(Integer userId, SaleSearchRequestDto searchCondition, Pageable pageable) {
        Page<SaleDto> salePage = saleRepository.searchSales(userId, searchCondition, pageable);
        List<SaleDto> sales = salePage.getContent();
        List<Integer> saleIds = getSaleIds(sales);
        List<SaleOrderCountDto> orderCounts = getOrderCounts(saleIds);
        List<SaleSearchResponseDto> saleSearchResponseDtos = saleMapper.mapToSaleSearchResponseDto(saleIds, sales, orderCounts);

        return new PageResponseDto<>(saleSearchResponseDtos, salePage);
    }

    @Transactional(readOnly = true)
    public PageResponseDto<SaleSearchResponseDto> searchSalesAdmin(SaleSearchRequestDto searchCondition, Pageable pageable) {
        Page<SaleDto> salePage = saleRepository.searchSalesAdmin(searchCondition, pageable);
        List<SaleDto> sales = salePage.getContent();
        List<Integer> saleIds = getSaleIds(sales);
        List<SaleOrderCountDto> orderCounts = getOrderCounts(saleIds);
        List<SaleSearchResponseDto> saleSearchResponseDtos = saleMapper.mapToSaleSearchResponseDto(saleIds, sales, orderCounts);

        return new PageResponseDto<>(saleSearchResponseDtos, salePage);
    }

    private List<SaleOrderCountDto> getOrderCounts(List<Integer> saleIds) {
        List<SaleOrderCountDto> orderCounts = new ArrayList<>();
        if (!saleIds.isEmpty()) {
            orderCounts = saleRepository.searchSaleOrderCount(saleIds);
        }
        return orderCounts;
    }

    private List<Integer> getSaleIds(List<SaleDto> sales) {
        return sales
                .stream()
                .map(SaleDto::getSaleId)
                .toList();
    }
}
