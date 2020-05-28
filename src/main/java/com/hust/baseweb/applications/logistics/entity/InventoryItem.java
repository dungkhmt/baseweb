package com.hust.baseweb.applications.logistics.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;


@Entity
@Getter
@Setter

public class InventoryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "inventory_item_id")
    private UUID inventoryItemId;

    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    @OneToOne(fetch = FetchType.EAGER)
    private Product product;

    @JoinColumn(name = "facility_id", referencedColumnName = "facility_id")
    @OneToOne(fetch = FetchType.EAGER)
    private Facility facility;

    @Column(name = "lot_id")
    private String lotId;

    @Column(name = "uom_id")
    private String uomId;

    @Column(name = "quantity_on_hand_total")
    private int quantityOnHandTotal;

    private Date createdStamp;
    private Date lastUpdatedStamp;

    public InventoryItem() {
        super();

    }


}
