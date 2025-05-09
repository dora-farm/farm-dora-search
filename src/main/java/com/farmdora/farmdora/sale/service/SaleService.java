package com.farmdora.farmdora.sale.service;

import com.farmdora.farmdora.common.NcpImageProperties;
import com.farmdora.farmdora.common.exception.ResourceNotFoundException;
import com.farmdora.farmdora.common.response.PageResponseDto;
import com.farmdora.farmdora.entity.Option;
import com.farmdora.farmdora.entity.Review;
import com.farmdora.farmdora.entity.ReviewFile;
import com.farmdora.farmdora.entity.Sale;
import com.farmdora.farmdora.entity.SaleFile;
import com.farmdora.farmdora.opinion.repository.QuestionRepository;
import com.farmdora.farmdora.opinion.repository.ReviewRepository;
import com.farmdora.farmdora.sale.dto.CategorySearchRequestDto;
import com.farmdora.farmdora.sale.dto.QuestionResponseDto;
import com.farmdora.farmdora.sale.dto.ReviewDetailDto;
import com.farmdora.farmdora.sale.dto.SaleDetailDto;
import com.farmdora.farmdora.sale.dto.SaleRankingDto;
import com.farmdora.farmdora.sale.dto.SaleRelatedDto;
import com.farmdora.farmdora.sale.dto.SaleRelatedInfoDto;
import com.farmdora.farmdora.sale.dto.SaleSummaryDto;
import com.farmdora.farmdora.sale.repository.LikeRepository;
import com.farmdora.farmdora.sale.repository.OptionRepository;
import com.farmdora.farmdora.sale.repository.ReviewFileRepository;
import com.farmdora.farmdora.sale.repository.SaleFileRepository;
import com.farmdora.farmdora.sale.repository.SaleRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
    private final SaleRedisService saleRedisService;
    private final ReviewFileRepository reviewFileRepository;
    private final NcpImageProperties imageProperties;

    @Transactional(readOnly = true)
    public SaleDetailDto getSaleDetail(Integer userId, Integer saleId) {
        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new ResourceNotFoundException("Sale", saleId));
        List<Option> options = optionRepository.findAllBySale(sale);
        List<SaleFile> saleImages = addImagePath(saleFileRepository.findAllBySale(sale));

        if (userId == null) {
            return SaleDetailDto.createSaleDetail(sale, saleImages, options, false);
        }

        boolean exists = likeRepository.existsByUserUserIdAndSaleId(userId, saleId);

        return SaleDetailDto.createSaleDetail(sale, saleImages, options, exists);
    }

    private List<SaleFile> addImagePath(List<SaleFile> saleImages) {
        return saleImages.stream()
                .map(file -> SaleFile.builder()
                        .id(file.getId())
                        .saveFile(imageProperties.getProduct().createImageUrl(file.getSaveFile()))
                        .sale(file.getSale())
                        .isMain(file.isMain())
                        .originFile(file.getOriginFile())
                        .build())
                .collect(Collectors.toList());
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
            boolean isLike = likeRepository.existsByUserUserIdAndSaleId(userId, relatedSale.getSaleId());
            Optional<SaleFile> saleFile = saleFileRepository.findBySaleIdAndIsMainIsFalse(relatedSale.getSaleId());
            relatedSales.add(SaleRelatedDto.createSaleRelatedDto(relatedSale, getMainImage(saleFile), isLike));
        }
        return relatedSales;
    }

    private String getMainImage(Optional<SaleFile> saleFile) {
        String mainImage = null;
        if (saleFile.isPresent()) {
            mainImage = imageProperties.getProduct().createImageUrl(saleFile.get().getSaveFile());
        }
        return mainImage;
    }

    @Transactional(readOnly = true)
    public PageResponseDto<ReviewDetailDto> getSaleReviews(Integer saleId, Pageable pageable) {
        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new ResourceNotFoundException("Sale", saleId));

        Page<Review> reviews = reviewRepository.findAllBySale(sale, pageable);

        List<Integer> reviewIds = reviews.getContent()
                .stream()
                .map(Review::getId)
                .toList();

        List<ReviewFile> reviewFiles = reviewFileRepository.findByReviewIdIn(reviewIds);

        Map<Integer, List<ReviewFile>> reviewFileMap = reviewFiles.stream()
                .collect(Collectors.groupingBy(rf -> rf.getReview().getId()));

        List<ReviewDetailDto> reviewDetails = reviews.getContent()
                .stream()
                .map(r -> ReviewDetailDto.fromEntity(
                        r,
                        reviewFileMap.getOrDefault(r.getId(), List.of())
                                .stream()
                                .map(f -> ReviewFile.builder()
                                        .id(f.getId())
                                        .saveFile(imageProperties.getReview().createImageUrl(f.getSaveFile()))
                                        .review(f.getReview())
                                        .build()
                                ).collect(Collectors.toList())
                ))
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

    @Transactional(readOnly = true)
    public PageResponseDto<SaleRankingDto> getTop50Sales(Integer userId, Pageable pageable) {
        List<SaleRankingDto> cachedSaleRanks = saleRedisService.findSaleRanks(pageable.getPageNumber());
        Integer count = saleRedisService.findSaleRankCount();

        if (cachedSaleRanks == null || cachedSaleRanks.isEmpty() || count == null) {
            log.info("캐시된 데이터가 존재하지 않습니다...DB를 조회합니다...");
            return getTop50SalesFromDb(userId, pageable);
        }

        return toPageResponseWithLikeAndImage(cachedSaleRanks, count, userId, pageable);
    }

    private PageResponseDto<SaleRankingDto> getTop50SalesFromDb(Integer userId, Pageable pageable) {
        Page<SaleRankingDto> sales = saleRepository.findTop50ByOrderCount(pageable);
        List<SaleRankingDto> content = sales.getContent();

        long totalElements = Math.min(50L, content.size());

        return toPageResponseWithLikeAndImage(content, totalElements, userId, pageable);
    }

    private PageResponseDto<SaleRankingDto> toPageResponseWithLikeAndImage(
            List<SaleRankingDto> dtos,
            long totalElements,
            Integer userId,
            Pageable pageable) {

        Set<Integer> likedSaleIds = Set.of();
        if (userId != null) {
            likedSaleIds = likeRepository.findSaleIdsByUserId(userId);
        }

        for (SaleRankingDto sale : dtos) {
            sale.setLiked(userId != null && likedSaleIds.contains(sale.getSaleId()));
            sale.setImageUrl(imageProperties.getProduct().createImageUrl(sale.getImageUrl()));
        }

        Page<SaleRankingDto> page = new PageImpl<>(dtos, pageable, totalElements);
        return new PageResponseDto<>(dtos, page);
    }

    @Transactional(readOnly = true)
    public PageResponseDto<SaleSummaryDto> getSalesByCategory(Integer userId,
                                                              CategorySearchRequestDto searchCondition,
                                                              Pageable pageable) {
        Page<SaleSummaryDto> sales = saleRepository.searchSalesByCategories(userId, searchCondition, pageable);

        sales.getContent().forEach(s -> {
            if (s.getMainImage() != null) {
                s.setMainImage(imageProperties.getProduct().createImageUrl(s.getMainImage()));
            }
        });

        return new PageResponseDto<>(sales.getContent(), sales);
    }
}