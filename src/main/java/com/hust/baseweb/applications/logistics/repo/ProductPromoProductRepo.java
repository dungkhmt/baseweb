package com.hust.baseweb.applications.logistics.repo;

import com.hust.baseweb.applications.logistics.entity.ProductPromoProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface ProductPromoProductRepo extends JpaRepository<ProductPromoProduct, String> {

    UUID findProductPromoProductByProductId(String productId);
}
