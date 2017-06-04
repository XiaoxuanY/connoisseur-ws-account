package com.connoisseur.services.manager;

import com.connoisseur.services.AccountUtils;
import com.connoisseur.services.model.Rating;
import com.connoisseur.services.repository.RatingRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by lawrence on 5/24/17.
 */

@Component
public class RatingManager {
    protected static final Logger log = Logger.getLogger(UserManager.class);

    @Autowired
    private RatingRepository ratingRepository;

    public Page<Rating> readPageRating(String userId, Pageable pageable) {
        long uid = Long.parseLong(userId);
        Iterable<Rating> iterable = ratingRepository.findAll(pageable);
        List<Rating> ratingList = new LinkedList<>();
        for (Rating r : iterable) {
            if (r.getUserId() == uid) {
                ratingList.add(r);
            }
        }
        return new PageImpl<>(ratingList, pageable, ratingList.size());
    }

    public Rating createRating(String userId, String restaurantId, String rating) {
        return ratingRepository.save(new Rating(Long.parseLong(userId), restaurantId, Integer.valueOf(rating)));
    }

    public Rating updateRating(String userId, String restaurantId, String rating) {
        return ratingRepository.save(new Rating(Long.parseLong(userId), restaurantId, Integer.valueOf(rating)));
    }

    public Rating readRating(String userId, String restaurantId) {
        return ratingRepository.findByUserIdAndRestaurantId(Long.parseLong(userId), restaurantId);
    }

    public Rating deleteRating(String userId, String restaurantId) {
        Rating rating = ratingRepository.findByUserIdAndRestaurantId(Long.parseLong(userId), restaurantId);
        ratingRepository.delete(rating);
        return rating;
    }
}
