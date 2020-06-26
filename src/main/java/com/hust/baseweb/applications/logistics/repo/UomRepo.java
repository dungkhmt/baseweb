package com.hust.baseweb.applications.logistics.repo;

import com.hust.baseweb.applications.logistics.entity.Uom;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface UomRepo extends PagingAndSortingRepository<Uom, String> {

    Uom findByUomId(String uomId);

    List<Uom> findAll();


}
