package com.connoisseur.services.repository;

import com.connoisseur.services.model.Bookmark;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * Created by lawrence on 5/24/17.
 */

@RepositoryRestResource(exported = false)
public interface BookmarkRepository extends PagingAndSortingRepository<Bookmark, Long> {
    List<Bookmark> findByUserId(long userId);
    Bookmark findByUserIdAndRestaurantId(long userId, String restaurantId);
}
