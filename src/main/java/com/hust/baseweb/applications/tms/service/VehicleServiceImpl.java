package com.hust.baseweb.applications.tms.service;

import com.hust.baseweb.applications.tms.entity.Vehicle;
import com.hust.baseweb.applications.tms.repo.VehicleRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class VehicleServiceImpl implements VehicleService {

    private VehicleRepo vehicleRepo;

    @Override
    public Page<Vehicle> findAll(Pageable pageable) {
        return vehicleRepo.findAll(pageable);
    }

    @Override
    public void save(Vehicle vehicle) {
        vehicleRepo.save(vehicle);
    }

    @Override
    public void saveAll(List<Vehicle> vehicles) {
        vehicleRepo.saveAll(vehicles);
    }
}
