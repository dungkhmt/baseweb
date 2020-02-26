package hust.baseweb.baseweb.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.util.Date;

@Entity
@Getter
@Setter
public class SecurityGroupPermission {
    @EmbeddedId
    private SecurityGroupPermissionId id;

    @Column(nullable = false)
    private Date createdAt;
}
