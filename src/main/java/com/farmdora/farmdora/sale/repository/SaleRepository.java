package com.farmdora.farmdora.sale.repository;

import com.farmdora.farmdora.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleRepository extends JpaRepository<Sale, Integer>, CustomSaleRepository {
}
