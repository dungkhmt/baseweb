package com.hust.baseweb.applications.logistics.repo;

import java.util.Date;
import java.util.UUID;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.hust.baseweb.applications.logistics.entity.Product;
import com.hust.baseweb.applications.logistics.entity.ProductPrice;

public interface ProductPriceRepo extends PagingAndSortingRepository<ProductPrice, UUID>{
	ProductPrice findByProductAndThruDate(Product product, Date thruDate);
}
