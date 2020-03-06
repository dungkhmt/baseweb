package hust.baseweb.baseweb.repository;

import hust.baseweb.baseweb.entity.SecurityGroupPermission;
import hust.baseweb.baseweb.entity.SecurityGroupPermissionId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SecurityGroupPermisisonRepository extends
        JpaRepository<SecurityGroupPermission, SecurityGroupPermissionId> {
    List<SecurityGroupPermission> findAll();
}
