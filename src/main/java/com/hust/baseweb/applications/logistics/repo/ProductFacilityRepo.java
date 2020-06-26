package com.hust.baseweb.applications.logistics.repo;

import com.hust.baseweb.applications.logistics.entity.CompositeProductFacilityId;
import com.hust.baseweb.applications.logistics.entity.ProductFacility;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductFacilityRepo extends JpaRepository<ProductFacility, CompositeProductFacilityId> {

    ProductFacility findByProductIdAndFacilityId(String productId, String facilityId);

    List<ProductFacility> findByProductIdInAndFacilityIdIn(List<String> productIds, List<String> facilityIds);

    List<ProductFacility> findAllByFacilityId(String facilityId);

    List<ProductFacility> findAllByProductIdIn(List<String> productIds);

    List<ProductFacility> findAllByFacilityIdAndProductIdIn(String facilityId, List<String> productIds);


}
