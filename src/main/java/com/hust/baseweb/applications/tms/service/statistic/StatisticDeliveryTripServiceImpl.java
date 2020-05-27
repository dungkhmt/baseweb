package com.hust.baseweb.applications.tms.service.statistic;

import com.hust.baseweb.applications.tms.entity.DeliveryTrip;
import com.hust.baseweb.applications.tms.model.VehicleModel;
import com.hust.baseweb.applications.tms.repo.DeliveryTripRepo;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Log4j2
public class StatisticDeliveryTripServiceImpl implements
    StatisticDeliveryTripService {

    private DeliveryTripRepo deliveryTripRepo;

    @Override
    public List<VehicleModel.Distance> collectVehicleDistance(
        String fromDate,
        String thruDate) {
        // TODO Auto-generated method stub
        List<DeliveryTrip> deliveryTrips = deliveryTripRepo.findAll();
        HashMap<String, Double> mVehicle2Distance = new HashMap<String, Double>();
        for (DeliveryTrip t : deliveryTrips) {
            String vehicleId = t.getVehicle().getVehicleId();
            double distance = t.getDistance();
            if (mVehicle2Distance.get(vehicleId) == null) {
                mVehicle2Distance.put(vehicleId, distance);
            } else {
                mVehicle2Distance.put(vehicleId, mVehicle2Distance.get(vehicleId) + distance);
            }
        }
        List<VehicleModel.Distance> lst = new ArrayList<>();
        for (String vehicleId : mVehicle2Distance.keySet()) {
            lst.add(new VehicleModel.Distance(vehicleId, mVehicle2Distance.get(vehicleId)));
        }
        return lst;
    }

}
