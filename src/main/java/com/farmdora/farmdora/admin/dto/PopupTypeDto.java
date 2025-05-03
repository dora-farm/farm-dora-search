package com.farmdora.farmdora.admin.dto;

import com.farmdora.farmdora.entity.PopupType;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PopupTypeDto {
    private Integer id;
    private String name;

    public static PopupTypeDto fromEntity(PopupType popupType) {
        return PopupTypeDto.builder()
                .id((int) popupType.getId())
                .name(popupType.getName())
                .build();
    }
}
