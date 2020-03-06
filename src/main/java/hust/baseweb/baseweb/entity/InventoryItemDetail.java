package hust.baseweb.baseweb.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
public class InventoryItemDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @NonNull
    private long inventoryItemId;

    @NonNull
    private BigDecimal exportedQuantity;

    @NonNull
    private Date effectiveFrom;

    @NonNull
    private long saleOrderId;

    @NonNull
    private short saleOrderSeq;

    private Date createdAt;
    private Date updatedAt;
}
