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

    public Rating createRating(Rating rating) {
        return ratingRepository.save(rating);
    }

    public Rating updateRating(Rating rating) {
        return ratingRepository.save(rating);
    }

    public Rating readRating(String userId, String restId) {
        return ratingRepository.findByUserIdAndRestaurantId(Long.parseLong(userId), restId);
    }

    public Rating deleteRating(String userId, String restId) {
        Rating rating = ratingRepository.findByUserIdAndRestaurantId(Long.parseLong(userId), restId);
        ratingRepository.delete(rating);
        return rating;
    }
}
