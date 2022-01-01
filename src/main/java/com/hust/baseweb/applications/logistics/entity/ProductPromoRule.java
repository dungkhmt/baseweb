package com.hust.baseweb.applications.logistics.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "product_promo_rule")
public class ProductPromoRule {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "product_promo_rule_id")
    private UUID productPromoRuleId;

    @Column(name = "product_promo_id")
    private UUID productPromoId;

    @Column(name = "product_promo_rule_enum_id")
    private String productPromoRuleEnumId;

    @Column(name = "rule_name")
    private String ruleName;

    @Column(name = "json_params")
    private String jsonParams;

    private String createdStamp;
    private String lastUpdatedStamp;

    @ManyToMany(mappedBy = "productPromoRules")
    private List<Product> products;
}
