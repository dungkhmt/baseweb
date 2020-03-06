package hust.baseweb.baseweb.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
public class SecurityPermission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonNull
    private short id;

    @Column(unique = true)
    @NonNull
    private String name;

    private Date createdAt;
}
