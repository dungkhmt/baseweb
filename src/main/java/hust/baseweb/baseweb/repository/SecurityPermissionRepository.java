package hust.baseweb.baseweb.repository;

import hust.baseweb.baseweb.entity.SecurityPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface SecurityPermissionRepository extends JpaRepository<SecurityPermission, Short> {

    @Query("select securityPermission from SecurityPermission securityPermission " +
            "join SecurityGroupPermission securityGroupPermission " +
            "on securityGroupPermission.id.securityPermissionId = securityPermission.id " +
            "join SecurityGroup securityGroup " +
            "on securityGroupPermission.id.securityGroupId = securityGroup.id " +
            "join UserLoginSecurityGroup userLoginSecurityGroup " +
            "on userLoginSecurityGroup.id.securityGroupId = securityGroup.id " +
            "where userLoginSecurityGroup.id.userLoginId = ?1")
    List<SecurityPermission> getAllByUserId(UUID userId);
}
