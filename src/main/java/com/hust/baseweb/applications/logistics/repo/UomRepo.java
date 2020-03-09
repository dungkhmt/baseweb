package com.hust.baseweb.applications.logistics.repo;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.hust.baseweb.applications.logistics.entity.Uom;

import java.util.List;

public interface UomRepo extends PagingAndSortingRepository<Uom, String> {
	public Uom findByUomId(String uomId);

	public List<Uom> findAll();
	
}
