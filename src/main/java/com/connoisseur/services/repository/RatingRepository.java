package com.connoisseur.services.repository;

import com.connoisseur.services.model.Rating;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * Created by lawrence on 5/24/17.
 */

@RepositoryRestResource(exported = false)
public interface RatingRepository extends PagingAndSortingRepository<Rating, Long> {
    List<Rating> findByUserId(long userId);
    Rating findByUserIdAndRestaurantId(long userId, String restaurantId);
}