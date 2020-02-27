package com.hust.baseweb.applications.logistics.repo;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hust.baseweb.applications.logistics.entity.Product;
import com.hust.baseweb.applications.logistics.entity.ProductPrice;

public interface ProductPriceJpaRepo extends JpaRepository<ProductPrice, UUID> {
	List<ProductPrice> findByProductAndThruDate(Product product, Date thruDate);
}
