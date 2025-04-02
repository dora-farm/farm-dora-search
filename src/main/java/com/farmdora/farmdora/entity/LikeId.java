package com.farmdora.farmdora.entity;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
@EqualsAndHashCode
public class LikeId implements Serializable {
    private Integer userId;
    private Integer saleId;
}
