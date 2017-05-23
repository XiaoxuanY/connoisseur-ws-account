package com.connoisseur.services.restcontroller;

import com.connoisseur.services.exception.ResourceNotFoundException;
import com.connoisseur.services.manager.UserManager;
import com.connoisseur.services.model.AuthToken;
import com.connoisseur.services.model.CnsUser;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    UserManager userManager;

    /**
     * GET /v1/user/#id
     */
    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public @ResponseBody
    CnsUser getUser(@PathVariable("id") String userId) {

        final CnsUser user;
        user = userManager.findUserByAny(userId);

        if (user == null) {
            throw new ResourceNotFoundException();
        }
        return user;

    }

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
     * GET /v1/user/loginsession/{token} auth
     */
    @RequestMapping(value = "/loginsession/{token}", method = RequestMethod.GET)
    public ResponseEntity<CnsUser> authSession(@PathVariable("token")String token) {
        CnsUser user = userManager.validateToken(token);
        user.setPassword("");
        return new ResponseEntity<>(user,HttpStatus.OK);
    }


}
