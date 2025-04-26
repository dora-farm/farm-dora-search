package com.farmdora.farmdora.admin.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
public class UserSearchResponseDto {
    private Integer userId;
    private String name;
    private Boolean isBlind;
    private Boolean isSeller;

    @QueryProjection
    public UserSearchResponseDto(Integer userId, String name, Boolean isBlind, Boolean isSeller) {
        this.userId = userId;
        this.name = name;
        this.isBlind = isBlind;
        this.isSeller = isSeller;
    }
}
