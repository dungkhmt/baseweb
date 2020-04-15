package com.hust.baseweb.applications.tms.repo;

import com.hust.baseweb.applications.tms.entity.ContDepotTruck;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ContDepotTruckRepo extends CrudRepository<ContDepotTruck,String> {
    List<ContDepotTruck> findAll();
    ContDepotTruck findByDepotTruckId(String id);
}
