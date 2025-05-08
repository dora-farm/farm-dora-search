package com.farmdora.farmdora.sale.repository;

import com.farmdora.farmdora.entity.Sale;
import com.farmdora.farmdora.entity.SaleType;
import com.farmdora.farmdora.sale.dto.SaleRankingDto;
import com.farmdora.farmdora.sale.dto.SaleRelatedInfoDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SaleRepository extends JpaRepository<Sale, Integer>, CustomSellerSaleRepository, CustomSaleRepository {

    @Query("SELECT new com.farmdora.farmdora.sale.dto.SaleRelatedInfoDto(s.id, s.title, MIN(o.price), AVG(r.score), COUNT(r)) " +
            "FROM Sale s " +
            "LEFT JOIN Review r ON r.sale = s " +
            "LEFT JOIN Option o ON o.sale = s " +
            "WHERE s.type = :type " +
            "AND s.id <> :excludedId " +
            "GROUP BY s.id, s.title " +
            "ORDER BY s.id DESC")
    List<SaleRelatedInfoDto> findTop10SalesWithReviewCountByTypeAndExcludedId(
            @Param("type") SaleType type,
            @Param("excludedId") Integer excludedId,
            Pageable pageable
    );

    @Query("SELECT new com.farmdora.farmdora.sale.dto.SaleRankingDto(" +
            "s.id, " +
            "s.title, " +
            "MIN(o.price), " +
            "COUNT(oo.id), " +
            "sf.saveFile) " +
            "FROM Sale s " +
            "JOIN Option o ON o.sale = s " +
            "JOIN OrderOption oo ON oo.option = o " +
            "LEFT JOIN SaleFile sf ON sf.sale = s AND sf.isMain = false " +
            "GROUP BY s.id, s.title, sf.saveFile " +
            "ORDER BY COUNT(oo.id) DESC, s.id DESC")
    Page<SaleRankingDto> findTop50ByOrderCount(Pageable pageable);
}
