package com.hust.baseweb.applications.tms.repo;

import com.hust.baseweb.applications.tms.entity.Vehicle;
import com.hust.baseweb.applications.tms.entity.VehicleLocationPriority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
public interface VehicleLocationPriorityRepo extends JpaRepository<VehicleLocationPriority, UUID> {

    List<VehicleLocationPriority> findAllByVehicleInAndThruDateIsNull(List<Vehicle> vehicles);
}
