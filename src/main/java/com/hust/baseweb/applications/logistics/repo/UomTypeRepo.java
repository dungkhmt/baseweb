package com.hust.baseweb.applications.logistics.repo;

import com.hust.baseweb.applications.logistics.entity.UomType;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UomTypeRepo extends PagingAndSortingRepository<UomType, String> {

    UomType findByUomTypeId(String uomTypeId);
}
