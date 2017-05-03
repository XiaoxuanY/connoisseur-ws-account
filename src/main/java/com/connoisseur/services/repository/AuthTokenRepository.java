package com.connoisseur.services.repository;

import com.connoisseur.services.model.AuthToken;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Ray Xiao on 4/21/17.
 */
public interface AuthTokenRepository extends CrudRepository<AuthToken, Long> {
    AuthToken findByToken(String token);


}


