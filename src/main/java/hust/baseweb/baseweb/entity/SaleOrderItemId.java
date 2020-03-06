package hust.baseweb.baseweb.entity;

import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@Embeddable
@EqualsAndHashCode
@RequiredArgsConstructor
@NoArgsConstructor
public class SaleOrderItemId implements Serializable {
    @NonNull
    private long saleOrderId;

    @NonNull
    private short saleOrderSeq;
}
