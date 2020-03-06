package hust.baseweb.baseweb.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.util.Date;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
public class SecurityGroupPermission {
    @EmbeddedId
    @NonNull
    private SecurityGroupPermissionId id;

    @Column(insertable = false)
    private Date createdAt;
}
