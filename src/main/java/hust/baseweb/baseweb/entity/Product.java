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
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NonNull
    @Column(unique = true)
    private String name;

    @NonNull
    private UUID createdByUserLoginId;

    @NonNull
    private String description;

    private BigDecimal weight;

    @NonNull
    private String weightUomId;

    @NonNull
    private String UnitUomId;

    private Date createdAt;
    private Date UpdatedAt;

}
