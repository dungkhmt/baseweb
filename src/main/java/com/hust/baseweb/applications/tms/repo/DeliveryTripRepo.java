package com.hust.baseweb.applications.tms.repo;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.hust.baseweb.applications.tms.entity.DeliveryTrip;

import java.util.UUID;

public interface DeliveryTripRepo extends PagingAndSortingRepository<DeliveryTrip, UUID> {

}
