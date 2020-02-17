package com.hust.baseweb.applications.tms.repo;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.hust.baseweb.applications.tms.entity.Vehicle;

public interface VehicleRepo extends
		PagingAndSortingRepository<Vehicle, String> {

}
