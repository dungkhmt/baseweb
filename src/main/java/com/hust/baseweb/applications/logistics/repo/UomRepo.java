package com.hust.baseweb.applications.logistics.repo;

import com.hust.baseweb.applications.logistics.entity.Uom;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UomRepo extends PagingAndSortingRepository<Uom, String> {
    public Uom findByUomId(String uomId);

}
