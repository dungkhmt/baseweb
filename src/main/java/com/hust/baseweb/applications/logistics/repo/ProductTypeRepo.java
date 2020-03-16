package com.hust.baseweb.applications.logistics.repo;

import com.hust.baseweb.applications.logistics.entity.ProductType;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ProductTypeRepo extends PagingAndSortingRepository<ProductType, String> {
    public List<ProductType> findAll();

    public ProductType findByProductTypeId(String productTypeId);
}
