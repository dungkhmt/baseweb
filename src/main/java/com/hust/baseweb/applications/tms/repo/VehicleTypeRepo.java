package com.hust.baseweb.applications.tms.repo;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.hust.baseweb.applications.tms.entity.VehicleType;

public interface VehicleTypeRepo extends PagingAndSortingRepository<VehicleType, String> {

}
