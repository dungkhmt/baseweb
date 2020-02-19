package com.hust.baseweb.applications.tms.repo;

import com.hust.baseweb.applications.tms.entity.DeliveryTrip;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface DeliveryTripRepo extends PagingAndSortingRepository<DeliveryTrip, UUID> {

}
