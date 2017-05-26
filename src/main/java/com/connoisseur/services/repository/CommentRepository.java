package com.connoisseur.services.repository;

import com.connoisseur.services.model.Comment;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * Created by lawrence on 5/24/17.
 */

@RepositoryRestResource(exported = false)
public interface CommentRepository extends PagingAndSortingRepository<Comment, Long> {
    List<Comment> findByUserId(long userId);
    Comment findByUserIdAndRestaurantId(long userId, String restaurantId);
}
