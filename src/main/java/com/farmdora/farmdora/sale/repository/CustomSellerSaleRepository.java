package com.farmdora.farmdora.sale.repository;

import com.farmdora.farmdora.sale.dto.SaleSearchRequestDto;
import com.farmdora.farmdora.sale.dto.querydsl.SaleDto;
import com.farmdora.farmdora.sale.dto.querydsl.SaleOrderCountDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomSellerSaleRepository {
    Page<SaleDto> searchSales(Integer sellerId, SaleSearchRequestDto searchCondition, Pageable pageable);
    List<SaleOrderCountDto> searchSaleOrderCount(List<Integer> saleIds);
}
