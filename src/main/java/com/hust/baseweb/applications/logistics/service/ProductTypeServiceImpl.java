package com.hust.baseweb.applications.logistics.service;

import com.hust.baseweb.applications.logistics.entity.ProductType;
import com.hust.baseweb.applications.logistics.repo.ProductTypeRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ProductTypeServiceImpl implements ProductTypeService {

    private ProductTypeRepo productTypeRepo;

    @Override
    public List<ProductType> getAllProductType() {

        return productTypeRepo.findAll();
    }

    @Override
    public ProductType getProductTypeByProductTypeId(String productTypeId) {

        return productTypeRepo.findByProductTypeId(productTypeId);
    }
}
