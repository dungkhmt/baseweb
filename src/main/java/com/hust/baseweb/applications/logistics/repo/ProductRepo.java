package com.hust.baseweb.applications.logistics.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hust.baseweb.applications.logistics.entity.Product;


public interface ProductRepo extends JpaRepository<Product,String>{
	Product findByProductId(String productId);
}
