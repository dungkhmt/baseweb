package com.hust.baseweb.applications.logistics.repo;

import com.hust.baseweb.applications.logistics.entity.ProductPriceSupplier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
public interface ProductPriceSupplierRepo extends JpaRepository<ProductPriceSupplier, UUID> {

}
