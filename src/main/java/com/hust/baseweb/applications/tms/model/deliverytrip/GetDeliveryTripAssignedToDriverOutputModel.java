package com.hust.baseweb.applications.tms.model.deliverytrip;

import com.hust.baseweb.applications.tms.entity.DeliveryTrip;
import com.hust.baseweb.applications.tms.entity.DeliveryTripDetail;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetDeliveryTripAssignedToDriverOutputModel {
	private DeliveryTripHeaderView[] deliveryTripHeader;
	
}
