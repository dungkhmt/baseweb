package com.hust.baseweb.applications.logistics.service;

import com.hust.baseweb.applications.logistics.entity.ProductFacility;
import org.springframework.stereotype.Service;

@Service
public interface ProductFacilityService {
    ProductFacility save(String productId, String facilityId, Double lastInventoryCount, Double atpInventoryCount);
}
