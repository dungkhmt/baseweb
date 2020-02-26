package com.hust.baseweb.applications.tms.entity;

import com.hust.baseweb.applications.tms.model.shipmentorder.ShipmentModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Setter
@Getter
public class Shipment {
    @Id
    @Column(name = "shipment_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID shipmentId;

    @Column(name = "shipment_type_id")
    private String shipmentTypeId;

    public ShipmentModel toShipmentModel() {
        return new ShipmentModel(
                shipmentId.toString(),
                shipmentTypeId
        );
    }
}
