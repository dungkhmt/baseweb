package com.hust.baseweb.applications.tms.service.statistic;

import com.hust.baseweb.applications.tms.model.VehicleModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StatisticDeliveryTripService {
    List<VehicleModel.Distance> collectVehicleDistance(String fromDate, String thruDate);
}
