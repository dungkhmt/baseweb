package hust.baseweb.baseweb.repository;

import hust.baseweb.baseweb.entity.SecurityGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SecurityGroupRepository extends JpaRepository<SecurityGroup, Short> {
    List<SecurityGroup> findAll();
}
