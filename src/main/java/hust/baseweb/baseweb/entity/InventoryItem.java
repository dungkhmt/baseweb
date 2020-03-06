package hust.baseweb.baseweb.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
public class InventoryItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NonNull
    private int productId;

    @NonNull
    private UUID facilityId;

    @NonNull
    private BigDecimal quantity;

    @NonNull
    private BigDecimal unitCost;

    @NonNull
    private String currencyUomId;

    private Date createdAt;
    private Date updatedAt;
}
