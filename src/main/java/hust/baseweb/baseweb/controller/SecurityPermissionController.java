package hust.baseweb.baseweb.controller;

import hust.baseweb.baseweb.entity.SecurityGroup;
import hust.baseweb.baseweb.entity.SecurityGroupPermission;
import hust.baseweb.baseweb.entity.SecurityGroupPermissionId;
import hust.baseweb.baseweb.entity.SecurityPermission;
import hust.baseweb.baseweb.repository.SecurityGroupPermisisonRepository;
import hust.baseweb.baseweb.repository.SecurityGroupRepository;
import hust.baseweb.baseweb.repository.SecurityPermissionRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
class PermissionResponse {
    private final List<SecurityGroup> securityGroups;
    private final List<SecurityPermission> securityPermissions;
    private final List<SecurityGroupPermission> securityGroupPermissions;
}

@Getter
@RequiredArgsConstructor
class SavePermissionsRequest {
    private final short securityGroupId;
    private final List<Short> toBeInserted;
    private final List<Short> toBeDeleted;
}

@Getter
@RequiredArgsConstructor
class SavePermissionsResponse {
    private final List<SecurityGroupPermission> securityGroupPermissions;
}

@RestController
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class SecurityPermissionController {
    private SecurityGroupRepository securityGroupRepository;
    private SecurityPermissionRepository securityPermissionRepository;
    private SecurityGroupPermisisonRepository securityGroupPermisisonRepository;

    @GetMapping("/api/security/permission")
    public PermissionResponse getAllGroupsAndPermissions() {
        return new PermissionResponse(
                securityGroupRepository.findAll(),
                securityPermissionRepository.findAll(),
                securityGroupPermisisonRepository.findAll()
        );
    }

    @PostMapping("/api/security/save-group-permissions")
    public SavePermissionsResponse saveGroupPermissions(@RequestBody SavePermissionsRequest body) {
        List<SecurityGroupPermission> toBeDeleted = body.getToBeDeleted()
                .stream()
                .map(permissionId -> new SecurityGroupPermission(
                        new SecurityGroupPermissionId(
                                body.getSecurityGroupId(), permissionId)))
                .collect(Collectors.toList());

        List<SecurityGroupPermission> toBeInserted = body.getToBeInserted()
                .stream()
                .map(permissionId -> new SecurityGroupPermission(
                        new SecurityGroupPermissionId(
                                body.getSecurityGroupId(), permissionId)))
                .collect(Collectors.toList());

        securityGroupPermisisonRepository.deleteAll(toBeDeleted);
        securityGroupPermisisonRepository.saveAll(toBeInserted);

        List<SecurityGroupPermission> items = securityGroupPermisisonRepository.findAll();
        return new SavePermissionsResponse(items);
    }
}
