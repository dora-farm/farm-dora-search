package com.farmdora.farmdora.opinion.repository;

import com.farmdora.farmdora.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Integer>, CustomReviewRepository {
    Page<Review> findAllBySaleId(Integer saleId, Pageable pageable);
}
