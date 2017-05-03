package com.connoisseur.services.repository;

import com.connoisseur.services.model.CnsUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


import java.util.List;

/**
 * Created by Ray Xiao on 4/18/17.
 */
@RepositoryRestResource
public interface UserRepository extends CrudRepository<CnsUser, Long> {
    CnsUser findByEmail(String email);

    List<CnsUser> findByLastName(String lastName);


}


