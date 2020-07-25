package com.hust.baseweb.applications.specialpurpose.saleslogmongo.repository;

import com.hust.baseweb.applications.specialpurpose.saleslogmongo.document.Facility;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Collection;
import java.util.List;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
public interface FacilityRepository extends MongoRepository<Facility, String> {

    Facility findByFacilityId(String facilityId);

    List<Facility> findAllByFacilityIdIn(Collection<String> facilityIds);
}
