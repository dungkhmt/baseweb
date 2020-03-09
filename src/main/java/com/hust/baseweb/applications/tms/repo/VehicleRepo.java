package com.hust.baseweb.applications.tms.repo;

import com.hust.baseweb.applications.tms.entity.Vehicle;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface VehicleRepo extends
        PagingAndSortingRepository<Vehicle, String> {

    List<Vehicle> findAllByVehicleIdIn(List<String> vehicleIds);

    Vehicle findByVehicleId(String vehicleId);
}
