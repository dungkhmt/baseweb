package com.hust.baseweb.applications.logistics.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.hust.baseweb.applications.logistics.entity.ProductFacility;

@Service
public interface ProductFacilityService {
	public ProductFacility save(String productId, String facilityId, BigDecimal lastInventoryCount, BigDecimal atpInventoryCount);
}
