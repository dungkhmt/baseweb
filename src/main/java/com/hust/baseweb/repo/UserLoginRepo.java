package com.hust.baseweb.repo;

import com.hust.baseweb.entity.Party;
import com.hust.baseweb.entity.UserLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface UserLoginRepo extends JpaRepository<UserLogin, String> {

    UserLogin findByUserLoginId(String userLoginId);

    List<UserLogin> findByParty(Party party);

    @Query(value= "select ul.*\n" +
                  "from user_login ul inner join public.user_login_security_group ulsg\n" +
                  "on ul.user_login_id = ulsg.user_login_id\n" +
                  "where ulsg.group_id = ?1",
           nativeQuery = true)
    List<UserLogin> findAllUserLoginsByGroupId(String groupId);

    @Query(value="select group_id\n" +
                 "from user_login_security_group\n" +
                 "where user_login_id = ?1",
           nativeQuery = true)
    List<String> findGroupPermsByUserLoginId(String userLoginId);
}
