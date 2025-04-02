package com.farmdora.farmdora.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "basket",
        uniqueConstraints = {
                @UniqueConstraint(name = "UIX_basket", columnNames = {"option_id", "user_id"})
        }
)
public class Basket {

    @EmbeddedId
    private BasketId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("optionId")
    @JoinColumn(name = "option_id", nullable = false)
    private Option option;

    public Basket(User user, Option option) {
        this.id = new BasketId(user.getUserId(), option.getId());
        this.user = user;
        this.option = option;
    }
}

