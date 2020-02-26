package hust.baseweb.baseweb.repository;

import hust.baseweb.baseweb.entity.UserLogin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserLoginRepository extends JpaRepository<UserLogin, UUID> {
    UserLogin findByUsername(String name);
}
