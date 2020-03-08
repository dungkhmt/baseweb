package com.hust.baseweb.applications.logistics.repo;

import com.hust.baseweb.applications.logistics.entity.Product;
import com.hust.baseweb.applications.logistics.entity.ProductPrice;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;
import java.util.UUID;

public interface ProductPriceRepo extends PagingAndSortingRepository<ProductPrice, UUID> {
    ProductPrice findByProductAndThruDate(Product product, Date thruDate);
}
