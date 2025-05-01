package com.farmdora.farmdora.admin.dto;

import com.farmdora.farmdora.entity.Popup;
import java.time.LocalDate;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PopupSearchResponseDto {
    private Integer popupId;
    private String title;
    private String imageUrl;
    private LocalDate startDate;
    private LocalDate endDate;
    private String type;

    public static PopupSearchResponseDto fromEntity(Popup popup, String imagePath, String imageType) {
        String imageUrl = null;
        if (popup.getSaveFile() != null) {
            imageUrl = imagePath + popup.getSaveFile() + imageType;
        }

        return PopupSearchResponseDto.builder()
                .popupId(popup.getId())
                .title(popup.getTitle())
                .imageUrl(imageUrl)
                .startDate(popup.getStartDate().toLocalDate())
                .endDate(popup.getEndDate().toLocalDate())
                .type(popup.getType().getName())
                .build();
    }
}
