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
public class ProductPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @NonNull
    private int productId;

    @NonNull
    private String currencyUomId;

    @NonNull
    private BigDecimal price;

    @NonNull
    private UUID createdByUserLoginId;

    @NonNull
    private Date effectiveFrom;
    private Date expiredAt;

    private Date createdAt;
    private Date UpdatedAt;

}
