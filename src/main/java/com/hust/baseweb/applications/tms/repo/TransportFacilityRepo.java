package com.hust.baseweb.applications.tms.repo;

import com.hust.baseweb.applications.tms.document.aggregation.TransportFacility;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
public interface TransportFacilityRepo extends MongoRepository<TransportFacility, TransportFacility.Id> {

    List<TransportFacility> findAllByIdIn(List<TransportFacility.Id> ids);

    List<TransportFacility> findAllById_FacilityIdAndId_DateBetween(String facilityId, LocalDate from, LocalDate to);
}
