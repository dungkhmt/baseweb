package com.hust.baseweb.repo;

import com.hust.baseweb.entity.UserLogin;
import org.springframework.data.repository.Repository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface UserLoginRepo extends Repository<UserLogin,String> {
    UserLogin findByUserLoginId(String userLoginId);
}
