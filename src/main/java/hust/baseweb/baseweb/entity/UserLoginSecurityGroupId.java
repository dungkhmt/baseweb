package hust.baseweb.baseweb.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@Setter
public class UserLoginSecurityGroupId implements Serializable {
    private UUID userLoginId;
    private short securityGroupId;
}