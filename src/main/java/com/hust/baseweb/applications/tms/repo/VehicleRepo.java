package com.hust.baseweb.applications.tms.repo;

import com.hust.baseweb.applications.tms.entity.Vehicle;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface VehicleRepo extends
        PagingAndSortingRepository<Vehicle, String> {

}
