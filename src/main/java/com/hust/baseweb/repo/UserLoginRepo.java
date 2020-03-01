package com.hust.baseweb.repo;

import java.util.List;

import com.hust.baseweb.entity.Party;
import com.hust.baseweb.entity.UserLogin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface UserLoginRepo extends JpaRepository<UserLogin, String> {
    UserLogin findByUserLoginId(String userLoginId);
    List<UserLogin> findByParty(Party party);
}
