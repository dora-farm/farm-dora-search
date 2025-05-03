package com.farmdora.farmdora.sale.repository;

import com.farmdora.farmdora.entity.ReviewFile;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewFileRepository extends JpaRepository<ReviewFile, Integer> {
    List<ReviewFile> findByReviewIdIn(List<Integer> reviewIds);
}
