package hust.baseweb.baseweb.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
public class SaleOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NonNull
    private UUID customerId;

    @NonNull
    private UUID originalFacilityId;

    @NonNull
    private UUID createdByUserLoginId;

    private String shipToAddress;
    private UUID shipToFacilityCustomerId;

    private Date createdAt;
    private Date updatedAt;
}
