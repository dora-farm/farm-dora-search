package com.farmdora.farmdora.admin.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
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
    private String id;
    private String name;
    private Boolean isBlind;
    private Boolean isSeller;
    private LocalDateTime createdDate;

    @QueryProjection
    public UserSearchResponseDto(Integer userId, String id, String name, Boolean isBlind, Boolean isSeller, LocalDateTime createdDate) {
        this.userId = userId;
        this.id = id;
        this.name = name;
        this.isBlind = isBlind;
        this.isSeller = isSeller;
        this.createdDate = createdDate;
    }
}
