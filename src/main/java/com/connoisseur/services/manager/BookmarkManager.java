package com.connoisseur.services.manager;

import com.connoisseur.services.model.Bookmark;
import com.connoisseur.services.model.Rating;
import com.connoisseur.services.repository.BookmarkRepository;
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
public class BookmarkManager {
    protected static final Logger log = Logger.getLogger(UserManager.class);

    @Autowired
    private BookmarkRepository bookmarkRepository;

    public Page<Bookmark> readPageBookmark(String userId, Pageable pageable) {
        long uid = Long.parseLong(userId);
        Iterable<Bookmark> iterable = bookmarkRepository.findAll(pageable);
        List<Bookmark> bookmarkList = new LinkedList<>();
        for (Bookmark b : iterable) {
            if (b.getUserId() == uid) {
                bookmarkList.add(b);
            }
        }
        return new PageImpl<>(bookmarkList, pageable, bookmarkList.size());
    }

    public Bookmark createBookmark(String userId, String restaurantId) {
        return bookmarkRepository.save(new Bookmark(Long.parseLong(userId), restaurantId));
    }

//    public Bookmark updateBookmark(String userId, String restaurantId) {
//        return bookmarkRepository.save(new Bookmark(Long.parseLong(userId), restaurantId));
//    }

    public Bookmark readBookmark(String userId, String restaurantId) {
        return bookmarkRepository.findByUserIdAndRestaurantId(Long.parseLong(userId), restaurantId);
    }

    public Bookmark deleteBookmark(String userId, String restaurantId) {
        Bookmark bookmark = bookmarkRepository.findByUserIdAndRestaurantId(Long.parseLong(userId), restaurantId);
        bookmarkRepository.delete(bookmark);
        return bookmark;
    }
}
