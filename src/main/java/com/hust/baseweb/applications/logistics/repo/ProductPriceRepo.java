package com.hust.baseweb.applications.logistics.repo;

import com.hust.baseweb.applications.logistics.entity.Product;
import com.hust.baseweb.applications.logistics.entity.ProductPrice;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface ProductPriceRepo extends PagingAndSortingRepository<ProductPrice, UUID> {
    ProductPrice findByProductAndThruDateNull(Product product);

    List<ProductPrice> findAllByProductInAndThruDateNull(Collection<Product> products);
}
