package hust.baseweb.baseweb.repository;

import hust.baseweb.baseweb.entity.SecurityGroupPermission;
import hust.baseweb.baseweb.entity.SecurityGroupPermissionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SecurityGroupPermisisonRepository extends
        JpaRepository<SecurityGroupPermission, SecurityGroupPermissionId> {

    @Query("select s from SecurityGroupPermission s where s.id.securityPermissionId = ?1")
    List<SecurityGroupPermission> getAllBySecurityPermissionId(short id);
}
