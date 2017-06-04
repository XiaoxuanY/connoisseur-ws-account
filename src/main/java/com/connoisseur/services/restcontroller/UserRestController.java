package com.connoisseur.services.restcontroller;

import com.connoisseur.services.exception.ResourceNotFoundException;
import com.connoisseur.services.manager.BookmarkManager;
import com.connoisseur.services.manager.CommentManager;
import com.connoisseur.services.manager.RatingManager;
import com.connoisseur.services.manager.UserManager;
import com.connoisseur.services.model.*;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by Ray Xiao on 4/18/17.
 */

@RestController
public class UserRestController {
    private static Gson gson = new Gson();

    public static class LoginInfo {
        @Getter
        @Setter
        private String email;
        @Getter
        @Setter
        private String password;
    }


    @Autowired
    private UserManager userManager;

    @Autowired
    private RatingManager ratingManager;

    @Autowired
    private BookmarkManager bookmarkManager;

    @Autowired
    private CommentManager commentManager;


    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<CnsUser> createUser(@RequestBody CnsUser user) {
        return new ResponseEntity<>(userManager.createUser(user), HttpStatus.OK);
    }

    @RequestMapping(value = "/loginsession", method = RequestMethod.POST)
    public ResponseEntity<String> authSession(@RequestBody LoginInfo loginInfo) {
        AuthToken token = userManager.auth(loginInfo.email, loginInfo.password);
        final String resultString = gson.toJson(token);
        return new ResponseEntity<>(resultString,HttpStatus.OK);
    }

    @RequestMapping(value = "/validsession", method = RequestMethod.GET)
    public ResponseEntity<CnsUser> authSession(@RequestParam("token") String token) {
        CnsUser user = userManager.validateToken(token);
        user.setPassword("");
        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public @ResponseBody
    CnsUser readUser(@RequestParam("uid") String userId) {

        final CnsUser user;
        user = userManager.findUserByAny(userId, true);

        if (user == null) {
            throw new ResourceNotFoundException();
        }
        return user;

    }

    @RequestMapping(value = "/user", method = RequestMethod.PUT)
    public @ResponseBody
    CnsUser updateUser(@RequestParam("uid") String userId , @RequestBody CnsUser updateUser) {

        final CnsUser existUser;
        existUser = userManager.findUserByAny(userId, true);

        if (existUser == null) {
            throw new ResourceNotFoundException();
        }

        userManager.updateUser(existUser, updateUser);;

        return existUser;
    }


    @RequestMapping(value="/user", method = RequestMethod.DELETE)
    public @ResponseBody
    CnsUser deleteUser(@RequestParam("uid") String userId) {
        final CnsUser existUser = userManager.findUserByAny(userId, false);

        if (existUser == null) {
            throw new ResourceNotFoundException();
        }

        return userManager.deleteUser(existUser);
    }

    @RequestMapping(value="/rating/uid/{userId}/limit/{limit}/page/{page}", method = RequestMethod.GET)
    public @ResponseBody
    Page<Rating> readAllRating(@PathVariable String userId, @PathVariable int limit, @PathVariable int page) {
        PageRequest pageRequest = new PageRequest(page, limit);
        return ratingManager.readPageRating(userId, pageRequest);
    }

    @RequestMapping(value="rating-restaurant/uid/{userId}/rid/{restaurantId}/rating/{rating}", method=RequestMethod.POST)
    public @ResponseBody
    Rating createRating(@PathVariable String userId, @PathVariable String restaurantId, @PathVariable String rating) {
        return ratingManager.createRating(userId, restaurantId, rating);
    }

    @RequestMapping(value="rating-restaurant/uid/{userId}/rid/{restaurantId}/rating/{rating}", method=RequestMethod.PUT)
    public @ResponseBody
    Rating updateRating(@PathVariable String userId, @PathVariable String restaurantId, @PathVariable String rating) {
        return ratingManager.updateRating(userId, restaurantId, rating);
    }

    @RequestMapping(value="rating-restaurant/uid/{userId}/rid/{restaurantId}", method=RequestMethod.GET)
    public @ResponseBody
    Rating readRating(@PathVariable String userId, @PathVariable String restaurantId) {
        return ratingManager.readRating(userId, restaurantId);
    }

    @RequestMapping(value="rating-restaurant/uid/{userId}/rid/{restaurantId}", method=RequestMethod.DELETE)
    public @ResponseBody
    Rating deleteRating(@PathVariable String userId, @PathVariable String restaurantId) {
        return ratingManager.deleteRating(userId, restaurantId);
    }

    @RequestMapping(value="/bookmark/uid/{userId}/limit/{limit}/page/{page}", method = RequestMethod.GET)
    public @ResponseBody
    Page<Bookmark> readAllBookmark(@PathVariable String userId, @PathVariable int limit, @PathVariable int page) {
        PageRequest pageRequest = new PageRequest(page, limit);
        return bookmarkManager.readPageBookmark(userId, pageRequest);
    }

    @RequestMapping(value="bookmark-restaurant/uid/{userId}/rid/{restaurantId}", method=RequestMethod.POST)
    public @ResponseBody
    Bookmark createBookmark(@PathVariable String userId, @PathVariable String restaurantId) {
        return bookmarkManager.createBookmark(userId, restaurantId);
    }

    @RequestMapping(value="bookmark-restaurant/uid/{userId}/rid/{restaurantId}", method=RequestMethod.GET)
    public @ResponseBody
    Bookmark readBookmark(@PathVariable String userId, @PathVariable String restaurantId) {
        return bookmarkManager.readBookmark(userId, restaurantId);
    }

    @RequestMapping(value="bookmark-restaurant/uid/{userId}/rid/{restaurantId}", method=RequestMethod.DELETE)
    public @ResponseBody
    Bookmark deleteBookmark(@PathVariable String userId, @PathVariable String restaurantId) {
        return bookmarkManager.deleteBookmark(userId, restaurantId);
    }

    @RequestMapping(value="/comment/uid/{userId}/limit/{limit}/page/{page}", method = RequestMethod.GET)
    public @ResponseBody
    Page<Comment> readAllComment(@PathVariable String userId, @PathVariable int limit, @PathVariable int page) {
        PageRequest pageRequest = new PageRequest(page, limit);
        return commentManager.readPageComment(userId, pageRequest);
    }

    @RequestMapping(value="comment-restaurant/uid/{userId}/rid/{restaurantId}", method=RequestMethod.POST)
    public @ResponseBody
    Comment createComment(@PathVariable String userId, @PathVariable String restaurantId, @RequestBody String comment) {
        return commentManager.createComment(userId, restaurantId, comment);
    }

    @RequestMapping(value="comment-restaurant/uid/{userId}/rid/{restaurantId}", method=RequestMethod.PUT)
    public @ResponseBody
    Comment updateComment(@PathVariable String userId, @PathVariable String restaurantId, @RequestBody String comment) {
        return commentManager.updateComment(userId, restaurantId, comment);
    }

    @RequestMapping(value="comment-restaurant/uid/{userId}/rid/{restaurantId}", method=RequestMethod.GET)
    public @ResponseBody
    Comment readComment(@PathVariable String userId, @PathVariable String restaurantId) {
        return commentManager.readComment(userId, restaurantId);
    }

    @RequestMapping(value="comment-restaurant/uid/{userId}/rid/{restaurantId}", method=RequestMethod.DELETE)
    public @ResponseBody
    Comment deleteComment(@PathVariable String userId, @PathVariable String restaurantId) {
        return commentManager.deleteComment(userId, restaurantId);
    }
}
