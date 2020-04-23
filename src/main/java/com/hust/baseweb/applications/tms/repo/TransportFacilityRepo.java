package com.hust.baseweb.applications.tms.repo;

import com.hust.baseweb.applications.tms.document.aggregation.TransportFacility;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
public interface TransportFacilityRepo extends MongoRepository<TransportFacility, TransportFacility.Id> {
    List<TransportFacility> findAllByIdIn(List<TransportFacility.Id> ids);
}
