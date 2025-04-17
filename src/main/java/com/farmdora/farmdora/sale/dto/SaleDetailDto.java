package com.farmdora.farmdora.sale.dto;

import com.farmdora.farmdora.entity.Option;
import com.farmdora.farmdora.entity.Sale;
import com.farmdora.farmdora.entity.SaleFile;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SaleDetailDto {

    @Builder.Default
    private List<String> files = new ArrayList<>();

    private List<OptionDetailDto> options;

    private String origin;
    private boolean isLike;
    private String content;

    public static SaleDetailDto createSaleDetail(Sale sale, List<SaleFile> files, List<Option> options, boolean isLike) {
        List<OptionDetailDto> optionDetails = options.stream()
                .map(OptionDetailDto::createOptionDetail)
                .toList();
        List<String> images = files.stream()
                .map(SaleFile::getSaveFile)
                .collect(Collectors.toList());

        return SaleDetailDto.builder()
                .files(images)
                .origin(sale.getOrigin())
                 .isLike(isLike)
                .options(optionDetails)
                .content(sale.getContent())
                .build();
    }

    @Getter
    @Setter
    @Builder
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OptionDetailDto {
        private Integer optionId;
        private String optionName;
        private Integer price;

        public static OptionDetailDto createOptionDetail(Option option) {
            return OptionDetailDto.builder()
                    .optionId(option.getId())
                    .optionName(option.getName())
                    .price(option.getPrice())
                    .build();
        }
    }
}
