package hust.baseweb.baseweb.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@Embeddable
public class SecurityGroupPermissionId implements Serializable {
    private short securityGroupId;
    private short securityPermissionId;
}