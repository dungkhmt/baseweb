package com.hust.baseweb.applications.tms.model.deliverytrip;

import com.hust.baseweb.applications.tms.model.DeliveryTripModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetDeliveryTripAssignedToDriverOutputModel {
    private DeliveryTripModel.HeaderView[] deliveryTripHeaders;

}
