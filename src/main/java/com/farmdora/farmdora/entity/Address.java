package com.farmdora.farmdora.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Address {

    @Column(nullable = false)
    private String addr;

    @Column(nullable = false)
    private String detailAddr;

    @Column(nullable = false, length = 5)
    private String postNum;
}
