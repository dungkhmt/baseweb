package com.hust.baseweb.applications.tms.repo;

import com.hust.baseweb.applications.tms.entity.Shipment;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface ShipmentRepo extends PagingAndSortingRepository<Shipment, UUID> {

}
