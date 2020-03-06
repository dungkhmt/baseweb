package hust.baseweb.baseweb.entity;

import lombok.*;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
public class SaleOrderItem {
    @EmbeddedId
    @NonNull
    private SaleOrderItemId id;

    @NonNull
    private int productId;

    @NonNull
    private UUID productPriceId;

    @NonNull
    private BigDecimal quantity;

    @NonNull
    private short saleOrderItemStatusId;

    private Date createdAt;
    private Date updatedAt;
}
