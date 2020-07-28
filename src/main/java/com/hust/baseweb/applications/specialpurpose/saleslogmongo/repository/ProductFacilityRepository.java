package com.hust.baseweb.applications.specialpurpose.saleslogmongo.repository;

import com.hust.baseweb.applications.specialpurpose.saleslogmongo.document.ProductFacility;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProductFacilityRepository extends MongoRepository<ProductFacility, String> {
    List<ProductFacility> findAllByProductIdAndFacilityId(String productId, String facilityId);
}
