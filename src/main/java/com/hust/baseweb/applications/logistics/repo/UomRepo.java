package com.hust.baseweb.applications.logistics.repo;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.hust.baseweb.applications.logistics.entity.Uom;

public interface UomRepo extends PagingAndSortingRepository<Uom, String> {
	public Uom findByUomId(String uomId);
	
}
