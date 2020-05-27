package com.hust.baseweb.applications.logistics.entity;

import com.hust.baseweb.applications.tms.entity.ShipmentItem;
import com.hust.baseweb.entity.Party;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
public class ShipmentItemRole {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "shipment_item_role_id")
    private UUID shipmentItemRoleId;

    @JoinColumn(name = "shipment_item_id", referencedColumnName = "shipment_item_id")
    @ManyToOne
    private ShipmentItem shipmentItem;

    @JoinColumn(name = "party_id", referencedColumnName = "party_id")
    @ManyToOne
    private Party party;

    @Column
    private String roleTypeId;

    @Column(name = "from_date")
    private Date fromDate;

    @Column(name = "thru_date")
    private Date thruDate;

}
