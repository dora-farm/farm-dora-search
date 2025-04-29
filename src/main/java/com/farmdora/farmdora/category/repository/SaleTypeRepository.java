package com.farmdora.farmdora.category.repository;

import com.farmdora.farmdora.entity.SaleType;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleTypeRepository extends JpaRepository<SaleType, Short> {

    @EntityGraph(attributePaths = {"saleTypeBig"})
    List<SaleType> findAll();
}
