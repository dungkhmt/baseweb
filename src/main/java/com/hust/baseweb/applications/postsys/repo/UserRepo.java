package com.hust.baseweb.applications.postsys.repo;

import com.hust.baseweb.entity.UserLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepo extends JpaRepository<UserLogin, UUID> {

    @Query("select roles from UserLogin ul where ul.userLoginId = ?1")
    String findSecurityGroupByUser(String username);

}

