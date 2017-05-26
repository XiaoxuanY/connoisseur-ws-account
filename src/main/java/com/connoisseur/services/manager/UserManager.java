package com.connoisseur.services.manager;

import com.connoisseur.services.AccountUtils;
import com.connoisseur.services.exception.CnsErrorCode;
import com.connoisseur.services.exception.CnsException;
import com.connoisseur.services.exception.InternalErrorException;
import com.connoisseur.services.model.AuthToken;
import com.connoisseur.services.model.CnsUser;
import com.connoisseur.services.model.UserStatus;
import com.connoisseur.services.repository.AuthTokenRepository;
import com.connoisseur.services.repository.UserRepository;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Created by Ray Xiao on 4/20/17.
 */

@Component
public class UserManager {
    protected static final Logger log = Logger.getLogger(UserManager.class);
    @Autowired
    AccountUtils accountUtils;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AuthTokenRepository authTokenRepository;


    private CnsUser findUserByAnyInternal(String identifier) {
        final CnsUser user;
        if (accountUtils.isEmpty(identifier)) {
            //maybe should throw exception
            log.info("Invalid identifier " + identifier);
            return null;
        }
        if (NumberUtils.isNumber(identifier)) {
            user = userRepository.findOne(Long.parseLong(identifier));

        } else {
            user = userRepository.findByEmail(identifier);
        }
        return user;
    }

    public CnsUser findUserByAny(String identifier, boolean nullPw) {
        final CnsUser user = findUserByAnyInternal(identifier);

        if (user != null && nullPw) {
            user.setPassword(null);
        }
        return user;
    }

    @Transactional(readOnly = true)
    public Page<CnsUser> searchUser(String query, int page, int limit) {

//        Page<CnsUser> result = userRepository.findAll();
        return null;

    }

//    private Pageable createPageRequest() {
//        //Create a new Pageable object here.
//    }

    public CnsUser createUser(CnsUser user) {
        final CnsUser existUser = userRepository.findByEmail(user.getEmail());
        if (existUser == null) {
            user.setPassword(accountUtils.getSaltedPassword(user.getEmail(), user.getPassword()));
            user.setStatus(UserStatus.P);
            return userRepository.save(user);
        } else {
            throw new InternalErrorException("CnsUser " + user.getEmail() + " already exists");
        }
    }

    public void updateUser(CnsUser existUser, CnsUser updateUser) {
        // if user does not exist, throw exception
        if (existUser == null) {
            throw new InternalErrorException("CnsUser with id " + updateUser.getId() + " does not exist");
        } else {
            // modify existing user
            existUser.setPassword(accountUtils.getSaltedPassword(updateUser.getEmail(), updateUser.getPassword()));
            existUser.setEmail(updateUser.getEmail());
            existUser.setUserName(updateUser.getUserName());
            existUser.setFirstName(updateUser.getFirstName());
            existUser.setLastName(updateUser.getLastName());
            // update item in the table
            userRepository.save(existUser);
        }
    }

    public CnsUser deleteUser(CnsUser existUser) {
        long userId = existUser.getId();
        AuthToken token = authTokenRepository.findByUserId(userId);

        // need to delete user's information from every repository
        authTokenRepository.delete(token);
        userRepository.delete(userId);
        return existUser;
    }

    public CnsUser validateToken(String token) {
        AuthToken authToken = authTokenRepository.findByToken(token);

        // token does not exist
        if (authToken == null) {
            throw new CnsException(CnsErrorCode.AUTH_TOKEN_INVALID, "token is" + token, "");
        }

        // when token expired, remove token
        if (authToken.isExpired()) {
            authTokenRepository.delete(authToken.getId());
            throw new CnsException(CnsErrorCode.AUTH_TOKEN_EXPIRED,
                    "for user id " + authToken.getId() + ", token:" + token, "");
        }

        // renew token's expiration
        authToken.touch();
        // save renewed token
        authTokenRepository.save(authToken);
        return userRepository.findOne(authToken.getUserId());
    }

    public AuthToken auth(String username, String password) {
        log.debug(String.format("start authentication for user %s", username));
        final CnsUser existUser = findUserByAnyInternal(username);

        if (existUser == null) {
            throw new RuntimeException(String.format("Failed to Authenticate user %s", username));
        } else {
            final String hashPassword = accountUtils.getSaltedPassword(existUser.getEmail(), password);
            log.info("found user with password" + existUser.getPassword() + ", compare with " + hashPassword);
            if (hashPassword.equals(existUser.getPassword())) {
                //authentication success
                AuthToken token = new AuthToken(existUser.getId(),UUID.randomUUID().toString());
                token = authTokenRepository.save(token);
                return token;

            } else {
                log.info(String.format("Reject login attempt for user %s [%s]", username));
                throw new RuntimeException("Wrong Password!");
            }
        }
    }
}
