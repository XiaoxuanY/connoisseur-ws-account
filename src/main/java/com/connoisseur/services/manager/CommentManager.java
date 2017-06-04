package com.connoisseur.services.manager;

import com.connoisseur.services.model.Bookmark;
import com.connoisseur.services.model.Comment;
import com.connoisseur.services.repository.BookmarkRepository;
import com.connoisseur.services.repository.CommentRepository;
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
public class CommentManager {
    protected static final Logger log = Logger.getLogger(UserManager.class);

    @Autowired
    private CommentRepository commentRepository;

    public Page<Comment> readPageComment(String userId, Pageable pageable) {
        long uid = Long.parseLong(userId);
        Iterable<Comment> iterable = commentRepository.findAll(pageable);
        List<Comment> commentList = new LinkedList<>();
        for (Comment c : iterable) {
            if (c.getUserId() == uid) {
                commentList.add(c);
            }
        }
        return new PageImpl<>(commentList, pageable, commentList.size());
    }

    public Comment createComment(String userId, String restaurantId, String comment) {
        return commentRepository.save(new Comment(Long.parseLong(userId), restaurantId, comment));
    }

    public Comment updateComment(String userId, String restaurantId, String comment) {
        return commentRepository.save(new Comment(Long.parseLong(userId), restaurantId, comment));
    }

    public Comment readComment(String userId, String restaurantId) {
        return commentRepository.findByUserIdAndRestaurantId(Long.parseLong(userId), restaurantId);
    }

    public Comment deleteComment(String userId, String restaurantId) {
        Comment comment = commentRepository.findByUserIdAndRestaurantId(Long.parseLong(userId), restaurantId);
        commentRepository.delete(comment);
        return comment;
    }
}
