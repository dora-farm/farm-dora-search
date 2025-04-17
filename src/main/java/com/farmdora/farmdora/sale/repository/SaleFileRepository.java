package com.farmdora.farmdora.sale.repository;

import com.farmdora.farmdora.entity.Sale;
import com.farmdora.farmdora.entity.SaleFile;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleFileRepository extends JpaRepository<SaleFile, Integer> {
    List<SaleFile> findAllBySale(Sale sale);
    Optional<SaleFile> findBySaleIdAndIsMainIsTrue(Integer saleId);
}
