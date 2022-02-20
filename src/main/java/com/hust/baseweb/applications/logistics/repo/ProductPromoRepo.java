package com.hust.baseweb.applications.logistics.repo;

import com.hust.baseweb.applications.logistics.entity.ProductPromo;
import com.hust.baseweb.applications.logistics.entity.ProductPromoProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface ProductPromoRepo extends JpaRepository<ProductPromo, String> {

    ProductPromo findProductPromoByProductPromoId(UUID productPromoId);
}
