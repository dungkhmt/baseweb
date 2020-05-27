package com.hust.baseweb.applications.logistics.service;

import com.hust.baseweb.applications.logistics.entity.ProductFacility;
import com.hust.baseweb.applications.logistics.repo.ProductFacilityRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ProductFacilityServiceImpl implements ProductFacilityService {

    private ProductFacilityRepo productFacilityRepo;

    @Override
    public ProductFacility save(
        String productId, String facilityId,
        int lastInventoryCount, int atpInventoryCount) {

        ProductFacility productFacility = new ProductFacility();
        productFacility.setAtpInventoryCount(atpInventoryCount);
        productFacility.setFacilityId(facilityId);
        productFacility.setLastInventoryCount(lastInventoryCount);
        productFacility.setProductId(productId);
        productFacility = productFacilityRepo.save(productFacility);
        return productFacility;
    }

}
