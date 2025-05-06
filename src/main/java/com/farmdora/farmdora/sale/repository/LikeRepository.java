package com.farmdora.farmdora.sale.repository;

import com.farmdora.farmdora.entity.Like;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LikeRepository extends JpaRepository<Like, Integer> {
    boolean existsByUserUserIdAndSaleId(Integer userId, Integer saleId);

    @Query("SELECT l.sale.id FROM Like l WHERE l.user.userId = :userId")
    Set<Integer> findSaleIdsByUserId(@Param("userId") Integer userId);
}
