package com.hust.baseweb.applications.logistics.repo;

import com.hust.baseweb.applications.logistics.entity.Product;
import com.hust.baseweb.applications.logistics.entity.ProductPromoRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ProductPromoRuleRepo extends JpaRepository<ProductPromoRule, String> {

    List<ProductPromoRule> findProductPromoRulesByProducts(Product product);
}
