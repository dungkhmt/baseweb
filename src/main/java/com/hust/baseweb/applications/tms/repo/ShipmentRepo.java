package com.hust.baseweb.applications.tms.repo;

import java.util.UUID;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.hust.baseweb.applications.tms.entity.Shipment;

public interface ShipmentRepo extends PagingAndSortingRepository<Shipment, UUID> {

}
