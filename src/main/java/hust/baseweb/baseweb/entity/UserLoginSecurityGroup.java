package hust.baseweb.baseweb.entity;

import lombok.*;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.util.Date;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
public class UserLoginSecurityGroup {
    @EmbeddedId
    @NonNull
    private UserLoginSecurityGroupId id;

    private Date createdAt;
}
