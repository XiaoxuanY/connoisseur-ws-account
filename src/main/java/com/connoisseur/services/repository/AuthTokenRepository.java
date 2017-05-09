package com.connoisseur.services.repository;

import com.connoisseur.services.model.AuthToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Created by Ray Xiao on 4/21/17.
 */
@RepositoryRestResource(exported = false)
public interface AuthTokenRepository extends CrudRepository<AuthToken, Long> {
    AuthToken findByToken(String token);

}


