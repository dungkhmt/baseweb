package com.hust.baseweb.applications.tms.entity.status;

import com.hust.baseweb.applications.tms.entity.DeliveryTrip;
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
public class DeliveryTripStatus {
    @Id
    private UUID deliveryTripStatusId;

    @JoinColumn(name = "delivery_trip_id", referencedColumnName = "delivery_trip_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private DeliveryTrip deliveryTrip;

    @JoinColumn(name = "status_id", referencedColumnName = "status_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private StatusItem statusItem;

    private Date fromDate;
    private Date thruDate;
}
