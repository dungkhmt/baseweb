package com.hust.baseweb.applications.tms.repo;

import com.hust.baseweb.applications.tms.entity.Vehicle;
import com.hust.baseweb.applications.tms.entity.VehicleMaintenanceHistory;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.UUID;

public interface VehicleMaintenanceHistoryRepo extends PagingAndSortingRepository<VehicleMaintenanceHistory, UUID> {

    List<VehicleMaintenanceHistory> findAllByVehicleIn(List<Vehicle> vehicles);
}
