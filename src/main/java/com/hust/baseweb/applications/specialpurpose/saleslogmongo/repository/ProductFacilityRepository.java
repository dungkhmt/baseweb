package com.hust.baseweb.applications.specialpurpose.saleslogmongo.repository;

import com.hust.baseweb.applications.specialpurpose.saleslogmongo.document.ProductFacility;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductFacilityRepository extends MongoRepository<ProductFacility, ProductFacility.ProductFacilityId> {
//    List<ProductFacility> findAllByProductIdAndFacilityId(String productId, String facilityId);
}
