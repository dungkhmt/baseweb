package hust.baseweb.baseweb.repository;

import hust.baseweb.baseweb.entity.UserLogin;
import hust.baseweb.baseweb.model.GetUserLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;
import java.util.List;

public interface UserLoginRepository extends JpaRepository<UserLogin, UUID> {
    UserLogin findByUsername(String name);

    @Query("select u.id as id, u.username as username, " +
            "u.createdAt as createdAt, " +
            "u.updatedAt as updatedAt from UserLogin u where u.username = ?1")
    GetUserLogin getUserLoginByUsername(String username);

    List<UserLogin> findAll();
}
