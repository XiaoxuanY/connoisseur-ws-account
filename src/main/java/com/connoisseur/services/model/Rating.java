package com.connoisseur.services.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by lawrence on 5/24/17.
 */
@Entity
public class Rating {

    @Id
    @Getter
    @GeneratedValue
    private long id;

    @Getter
    @Setter
    private long userId;

    @Getter
    @Setter
    private int rating;

    @Getter
    @Setter
    private String restaurantId;

    Rating() {} // jpa only?

    public Rating(long userId, String restaurantId, int rating) {
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.rating = rating;
    }
}
