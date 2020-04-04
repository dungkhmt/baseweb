package com.hust.baseweb.applications.tms.entity.status;

import com.hust.baseweb.applications.tms.entity.ShipmentItem;
import com.hust.baseweb.entity.StatusItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShipmentItemStatus {
    @Id
    private UUID shipmentItemStatusId;

    @JoinColumn(name = "shipment_item_id", referencedColumnName = "shipment_item_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private ShipmentItem shipmentItem;

    @JoinColumn(name = "status_id", referencedColumnName = "status_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private StatusItem statusItem;

    private Date fromDate;
    private Date thruDate;
}
