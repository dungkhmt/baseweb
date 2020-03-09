package com.hust.baseweb.applications.tms.model.deliverytrip;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetDeliveryTripAssignedToDriverOutputModel {
    private DeliveryTripHeaderView[] deliveryTripHeader;

}
