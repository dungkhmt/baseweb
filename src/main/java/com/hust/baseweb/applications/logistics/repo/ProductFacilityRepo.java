package com.hust.baseweb.applications.logistics.repo;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.hust.baseweb.applications.logistics.entity.CompositeProductFacilityId;
import com.hust.baseweb.applications.logistics.entity.ProductFacility;

public interface ProductFacilityRepo extends PagingAndSortingRepository<ProductFacility, CompositeProductFacilityId> {
	public ProductFacility findByProductIdAndFacilityId(String productId, String facilityId);
}
