package com.hust.baseweb.applications.tms.repo;

import com.hust.baseweb.applications.tms.entity.Vehicle;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.UUID;

public interface VehicleRepo extends
        PagingAndSortingRepository<Vehicle, String> {

    List<Vehicle> findAllByVehicleIdIn(List<UUID> vehicleIds);
}
