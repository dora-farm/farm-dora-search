package com.farmdora.farmdora.sale.service;

import com.farmdora.farmdora.common.exception.ResourceNotFoundException;
import com.farmdora.farmdora.common.response.PageResponseDto;
import com.farmdora.farmdora.entity.Option;
import com.farmdora.farmdora.entity.Sale;
import com.farmdora.farmdora.entity.SaleFile;
import com.farmdora.farmdora.sale.dto.SaleDetailDto;
import com.farmdora.farmdora.sale.dto.SaleSearchRequestDto;
import com.farmdora.farmdora.sale.dto.SaleSearchResponseDto;
import com.farmdora.farmdora.sale.dto.querydsl.SaleDto;
import com.farmdora.farmdora.sale.dto.querydsl.SaleOrderCountDto;
import com.farmdora.farmdora.sale.mapper.SaleMapper;
import com.farmdora.farmdora.sale.repository.LikeRepository;
import com.farmdora.farmdora.sale.repository.OptionRepository;
import com.farmdora.farmdora.sale.repository.SaleFileRepository;
import com.farmdora.farmdora.sale.repository.SaleRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SaleService {
    private final SaleRepository saleRepository;
    private final OptionRepository optionRepository;
    private final SaleFileRepository saleFileRepository;
    private final LikeRepository likeRepository;
    private final SaleMapper saleMapper;

    @Transactional(readOnly = true)
    public PageResponseDto<SaleSearchResponseDto> searchSales(Integer sellerId, SaleSearchRequestDto searchCondition, Pageable pageable) {
        Page<SaleDto> salePage = saleRepository.searchSales(sellerId, searchCondition, pageable);
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

    @Transactional(readOnly = true)
    public SaleDetailDto getSaleDetail(Integer userId, Integer saleId) {
        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new ResourceNotFoundException("Sale", saleId));
        List<Option> options = optionRepository.findAllBySale(sale);
        List<SaleFile> saleImages = saleFileRepository.findAllBySale(sale);

        if (userId == null) {
            return SaleDetailDto.createSaleDetail(sale, saleImages, options, false);
        }

        boolean exists = likeRepository.existsByUserIdAndSaleId(userId, saleId);

        return SaleDetailDto.createSaleDetail(sale, saleImages, options, exists);
    }
}