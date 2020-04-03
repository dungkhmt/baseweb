package com.hust.baseweb.applications.tms.entity.status;

import com.hust.baseweb.applications.tms.entity.DeliveryTripDetail;
import com.hust.baseweb.entity.StatusItem;
import com.hust.baseweb.entity.UserLogin;
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
public class DeliveryTripDetailStatus {

    @Id
    private UUID deliveryTripDetailStatusId;

    @JoinColumn(name = "delivery_trip_detail_id", referencedColumnName = "delivery_trip_detail_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private DeliveryTripDetail deliveryTripDetail;

    @JoinColumn(name = "status_id", referencedColumnName = "status_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private StatusItem statusItem;

    private Date fromDate;
    private Date thruDate;

    @JoinColumn(name = "updated_by_user_login_id", referencedColumnName = "user_login_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private UserLogin userLogin;
}
