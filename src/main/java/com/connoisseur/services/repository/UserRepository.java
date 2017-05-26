package com.connoisseur.services.repository;

import com.connoisseur.services.model.CnsUser;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * Created by Ray Xiao on 4/18/17.
 */
@RepositoryRestResource(collectionResourceRel = "user", path = "user")
public interface UserRepository extends PagingAndSortingRepository<CnsUser, Long> {
    CnsUser findByEmail(String email);
    List<CnsUser> findByLastName(String lastName);
}