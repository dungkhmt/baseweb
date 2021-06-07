package com.hust.baseweb.applications.specialpurpose.saleslogmongo.repository;

import com.hust.baseweb.applications.specialpurpose.saleslogmongo.document.ProductFacility;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProductFacilityRepository extends MongoRepository<ProductFacility, ProductFacility.ProductFacilityId> {

    //    List<ProductFacility> findAllByProductIdAndFacilityId(String productId, String facilityId);
    List<ProductFacility> findAllById_FacilityIdAndQuantityOnHandGreaterThan(String facilityId, int quantityOnHand);

    List<ProductFacility> findAllById_FacilityId(String facilityId);
}
