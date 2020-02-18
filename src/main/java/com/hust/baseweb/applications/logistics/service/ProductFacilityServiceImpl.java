package com.hust.baseweb.applications.logistics.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;

import com.hust.baseweb.applications.logistics.entity.ProductFacility;
import com.hust.baseweb.applications.logistics.repo.ProductFacilityRepo;


public class ProductFacilityServiceImpl implements ProductFacilityService {

	@Autowired
	private ProductFacilityRepo productFacilityRepo;
	
	@Override
	public ProductFacility save(String productId, String facilityId,
			BigDecimal lastInventoryCount, BigDecimal atpInventoryCount) {
		// TODO Auto-generated method stub
		ProductFacility pf = new ProductFacility();
		pf.setAtpInventoryCount(atpInventoryCount);
		pf.setFacilityId(facilityId);
		pf.setLastInventoryCount(lastInventoryCount);
		pf.setProductId(productId);
		pf = productFacilityRepo.save(pf);
		return pf;
	}

}
