package com.farmdora.farmdora.sale.service;

import com.farmdora.farmdora.common.exception.ResourceNotFoundException;
import com.farmdora.farmdora.common.response.PageResponseDto;
import com.farmdora.farmdora.entity.Option;
import com.farmdora.farmdora.entity.Review;
import com.farmdora.farmdora.entity.Sale;
import com.farmdora.farmdora.entity.SaleFile;
import com.farmdora.farmdora.opinion.repository.QuestionRepository;
import com.farmdora.farmdora.opinion.repository.ReviewRepository;
import com.farmdora.farmdora.sale.dto.QuestionResponseDto;
import com.farmdora.farmdora.sale.dto.ReviewDetailDto;
import com.farmdora.farmdora.sale.dto.SaleDetailDto;
import com.farmdora.farmdora.sale.dto.SaleRelatedDto;
import com.farmdora.farmdora.sale.dto.SaleRelatedInfoDto;
import com.farmdora.farmdora.sale.repository.LikeRepository;
import com.farmdora.farmdora.sale.repository.OptionRepository;
import com.farmdora.farmdora.sale.repository.SaleFileRepository;
import com.farmdora.farmdora.sale.repository.SaleRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class SaleService {
    private final SaleRepository saleRepository;
    private final OptionRepository optionRepository;
    private final SaleFileRepository saleFileRepository;
    private final LikeRepository likeRepository;
    private final ReviewRepository reviewRepository;
    private final QuestionRepository questionRepository;

    @Value("${ncp.image.path}")
    private String imagePath;

    @Value("${ncp.image.type}")
    private String type;

    @Transactional(readOnly = true)
    public SaleDetailDto getSaleDetail(Integer userId, Integer saleId) {
        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new ResourceNotFoundException("Sale", saleId));
        List<Option> options = optionRepository.findAllBySale(sale);
        List<SaleFile> saleImages = saleFileRepository.findAllBySale(sale);

        if (userId == null) {
            return SaleDetailDto.createSaleDetail(sale, saleImages, options, false);
        }

        boolean exists = likeRepository.existsByUserIdAndSaleId(userId, saleId);

        return SaleDetailDto.createSaleDetail(sale, saleImages, options, exists);
    }

    @Transactional(readOnly = true)
    public List<SaleRelatedDto> getRelatedSales(Integer userId, Integer saleId, Pageable pageable) {
        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new ResourceNotFoundException("Sale", saleId));

        List<SaleRelatedInfoDto> relatedSaleDetails = saleRepository.findTop10SalesWithReviewCountByTypeAndExcludedId(sale.getType(), saleId, pageable);
        List<SaleRelatedDto> relatedSales = getRelatedSalesMainImageAndIsLike(userId, relatedSaleDetails);

        log.info("관련 상품 목록: {}", relatedSales);

        return relatedSales;
    }

    private List<SaleRelatedDto> getRelatedSalesMainImageAndIsLike(Integer userId, List<SaleRelatedInfoDto> relatedSaleDetails) {
        List<SaleRelatedDto> relatedSales = new ArrayList<>();
        for (SaleRelatedInfoDto relatedSale : relatedSaleDetails) {
            boolean isLike = likeRepository.existsByUserIdAndSaleId(userId, relatedSale.getSaleId());
            Optional<SaleFile> saleFile = saleFileRepository.findBySaleIdAndIsMainIsTrue(relatedSale.getSaleId());
            relatedSales.add(SaleRelatedDto.createSaleRelatedDto(relatedSale, getMainImage(saleFile), isLike));
        }
        return relatedSales;
    }

    private String getMainImage(Optional<SaleFile> saleFile) {
        String mainImage = null;
        if (saleFile.isPresent()) {
            mainImage = String.format("%s%s%s", imagePath, saleFile.get().getSaveFile(), type);
        }
        return mainImage;
    }

    @Transactional(readOnly = true)
    public PageResponseDto<ReviewDetailDto> getSaleReviews(Integer saleId, Pageable pageable) {
        Page<Review> reviews = reviewRepository.findAllBySaleId(saleId, pageable);
        List<ReviewDetailDto> reviewDetails = reviews.getContent()
                .stream()
                .map(ReviewDetailDto::fromEntity)
                .toList();
        return new PageResponseDto<>(reviewDetails, reviews);
    }

    @Transactional(readOnly = true)
    public PageResponseDto<QuestionResponseDto> getSaleQuestions(Integer saleId, Pageable pageable) {
        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new ResourceNotFoundException("Sale", saleId));

        Page<QuestionResponseDto> questions = questionRepository.findQuestionsBySaleId(sale, pageable);
        return new PageResponseDto<>(questions.getContent(), questions);
    }
}