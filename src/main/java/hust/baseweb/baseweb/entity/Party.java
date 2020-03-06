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
public class Party {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @NonNull
    private short partyTypeId;

    @NonNull
    private String description;

    @NonNull
    private UUID createdByUserLoginId;

    @NonNull
    private UUID updatedByUserLoginId;

    private Date createdAt;
    private Date updatedAt;
}
