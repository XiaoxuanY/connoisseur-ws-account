package com.connoisseur.services.restcontroller;

import com.connoisseur.services.exception.ResourceNotFoundException;
import com.connoisseur.services.manager.BookmarkManager;
import com.connoisseur.services.manager.CommentManager;
import com.connoisseur.services.manager.RatingManager;
import com.connoisseur.services.manager.UserManager;
import com.connoisseur.services.model.AuthToken;
import com.connoisseur.services.model.CnsUser;
import com.connoisseur.services.model.Rating;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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


    /**
     * POST /v1/user/  create new user
     * No X-Auth-Token Needed
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<CnsUser> createUser(@RequestBody CnsUser user) {
        return new ResponseEntity<>(userManager.createUser(user), HttpStatus.OK);
    }


    /**
     * POST /v1/user/loginsession auth
     * No X-Auth-Token Needed
     */
    @RequestMapping(value = "/loginsession", method = RequestMethod.POST)
    public ResponseEntity<String> authSession(@RequestBody LoginInfo loginInfo) {
        AuthToken token = userManager.auth(loginInfo.email, loginInfo.password);
        final String resultString = gson.toJson(token);
        return new ResponseEntity<>(resultString,HttpStatus.OK);
    }


    /**
     * GET /v1/user/validsession/{token} auth
     */
    @RequestMapping(value = "/validsession", method = RequestMethod.GET)
    public ResponseEntity<CnsUser> authSession(@RequestParam("token") String token) {
        CnsUser user = userManager.validateToken(token);
        user.setPassword("");
        return new ResponseEntity<>(user,HttpStatus.OK);
    }


    /**
     * GET /v1/user/#id
     */
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


    /**
     * PUT /v1/user/#id
     */
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

    @RequestMapping(value="/rating", method = RequestMethod.GET)
    public @ResponseBody
    Page<Rating> readAllRating(@RequestParam("uid") String userId, Pageable pageable) {
        return ratingManager.readPageRating(userId, pageable);
    }

    @RequestMapping(value="/rating-restaurant", method=RequestMethod.POST)
    public @ResponseBody
    Rating createRating(@RequestBody Rating rating) {
        return ratingManager.createRating(rating);
    }

//    @RequestMapping(value="rating-restaurant", method=RequestMethod.PUT)
//    public @ResponseBody
//    Rating updateRating(@RequestBody Rating rating) {
//        return ratingManager.updateRating(rating);
//    }
//
//    @RequestMapping(value="rating-restaurant", method=RequestMethod.GET)
//    public @ResponseBody
//    Rating readRating(@RequestParam Map<String,String> requestParams) {
//        String userId = requestParams.get("uid");
//        String restId = requestParams.get("rid");
//
//        return ratingManager.readRating(userId, restId);
//    }
//
//    @RequestMapping(value="rating-restaurant", method=RequestMethod.DELETE)
//    public @ResponseBody
//    Rating deleteRating(@RequestParam Map<String,String> requestParams) {
//        String userId = requestParams.get("uid");
//        String restId = requestParams.get("rid");
//
//        return ratingManager.deleteRating(userId, restId);
//    }

//
//    /**
//     * GET /v1/user/   get user list
//     *
//     * @return
//     */
//    @RequestMapping(value = "/user/",params={"limit","page","query"},method = RequestMethod.GET)
//    public @ResponseBody
//    List<CnsUser> getUser(@RequestParam("limit") int limit, @RequestParam("page") int page, @RequestParam("query") String query) {
//
//
//        final Page<CnsUser> result = userManager.searchUser(query,page,limit);
//
//        return result.getContent();
//
//
//    }
}
