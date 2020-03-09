package com.hust.baseweb.applications.logistics.service;

import com.hust.baseweb.applications.logistics.entity.ProductFacility;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public interface ProductFacilityService {
    ProductFacility save(String productId, String facilityId, BigDecimal lastInventoryCount, BigDecimal atpInventoryCount);
}
