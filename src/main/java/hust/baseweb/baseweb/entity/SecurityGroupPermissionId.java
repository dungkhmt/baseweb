package hust.baseweb.baseweb.entity;

import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@Embeddable
@EqualsAndHashCode
@RequiredArgsConstructor
@NoArgsConstructor
public class SecurityGroupPermissionId implements Serializable {
    @NonNull
    private short securityGroupId;

    @NonNull
    private short securityPermissionId;
}