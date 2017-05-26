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
public class Bookmark {

    @Id
    @Getter
    @GeneratedValue
    private long id;

    @Getter
    @Setter
    private long userId;

    @Getter
    @Setter
    private String restaurantId;

    Bookmark() {} // jpa only?

    Bookmark(long userId, String restaurantId) {
        this.userId = userId;
        this.restaurantId = restaurantId;
    }
}
