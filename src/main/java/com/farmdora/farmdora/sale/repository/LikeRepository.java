package com.farmdora.farmdora.sale.repository;

import com.farmdora.farmdora.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Integer> {
    boolean existsByUserIdAndSaleId(Integer userId, Integer saleId);
}
