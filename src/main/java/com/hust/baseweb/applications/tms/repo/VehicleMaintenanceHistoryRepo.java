package com.hust.baseweb.applications.tms.repo;

import com.hust.baseweb.applications.tms.entity.Vehicle;
import com.hust.baseweb.applications.tms.entity.VehicleMaintenanceHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.UUID;

public interface VehicleMaintenanceHistoryRepo extends PagingAndSortingRepository<VehicleMaintenanceHistory, UUID> {

    List<VehicleMaintenanceHistory> findAllByThruDateIsNullAndVehicleIn(List<Vehicle> vehicles);

    Page<VehicleMaintenanceHistory> findAllByThruDateIsNull(Pageable pageable);

    List<VehicleMaintenanceHistory> findAllByThruDateIsNull();
}
