package hust.baseweb.baseweb.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
public class Facility {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @NonNull
    @Column(unique = true)
    private String name;

    @NonNull
    private short facilityTypeId;

    @NonNull
    private String address;

    private Date createdAt;
    private Date updatedAt;
}
