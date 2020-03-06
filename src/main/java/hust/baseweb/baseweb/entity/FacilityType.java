package hust.baseweb.baseweb.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
public class FacilityType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonNull
    private short id;

    @NonNull
    @Column(unique = true)
    private String name;

    private Date createdAt;
}
