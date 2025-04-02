package com.farmdora.farmdora.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Seller {

    @Id
    @Column(name = "company_num")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_Id")
    private User user;

    @Column(nullable = false, length = 50)
    private String name;

    @Embedded
    private Address address;

    @Column(nullable = false, length = 30)
    private String phoneNum;

    @Column(nullable = false)
    private String saveFile;

    @Column(nullable = false)
    private String originFile;

    @Column(nullable = false)
    private boolean isApprove;
}
