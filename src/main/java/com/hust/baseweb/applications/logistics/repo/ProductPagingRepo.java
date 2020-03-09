package com.hust.baseweb.applications.logistics.repo;

import com.hust.baseweb.applications.logistics.entity.Product;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProductPagingRepo extends PagingAndSortingRepository<Product,String> {
}
