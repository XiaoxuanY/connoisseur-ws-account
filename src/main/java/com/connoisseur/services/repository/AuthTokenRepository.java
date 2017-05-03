package com.connoisseur.services.repository;

import com.connoisseur.services.model.AuthToken;
import com.connoisseur.services.model.CnsUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * Created by Ray Xiao on 4/21/17.
 */
@RepositoryRestResource
public interface AuthTokenRepository extends CrudRepository<AuthToken, Long> {
    AuthToken findByToken(String token);


}


