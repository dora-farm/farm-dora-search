package com.farmdora.farmdora.sale.repository;

import com.farmdora.farmdora.sale.dto.CategorySearchRequestDto;
import com.farmdora.farmdora.sale.dto.SaleSummaryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomSaleRepository {
    Page<SaleSummaryDto> searchSalesByCategories(Integer userId, CategorySearchRequestDto searchCondition, Pageable pageable);
}
