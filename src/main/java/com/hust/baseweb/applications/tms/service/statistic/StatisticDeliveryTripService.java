package com.hust.baseweb.applications.tms.service.statistic;

import org.springframework.stereotype.Service;

import com.hust.baseweb.applications.tms.model.statistic.vehicledistance.VehicleDistance;

import java.util.List;

@Service
public interface StatisticDeliveryTripService {
	List<VehicleDistance> collectVehicleDistance(String fromDate, String thruDate);
}
