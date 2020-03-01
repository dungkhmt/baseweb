package com.hust.baseweb.applications.tms.repo;

import com.hust.baseweb.applications.tms.entity.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface VehicleRepo extends
        PagingAndSortingRepository<Vehicle, String> {

    List<Vehicle> findAllByVehicleIdIn(List<String> vehicleIds);

    Page<Vehicle> findAllByVehicleMaintenanceHistory_ThruDateIsNull(Pageable pageable);

    List<Vehicle> findAllByVehicleMaintenanceHistory_ThruDateIsNull();
}
