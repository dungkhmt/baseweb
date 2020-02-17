package com.hust.baseweb.applications.tms.repo;

import java.util.UUID;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.hust.baseweb.applications.tms.entity.ShipmentItem;

public interface ShipmentItemRepo extends PagingAndSortingRepository<ShipmentItem, UUID> {

}
