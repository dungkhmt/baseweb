package com.hust.baseweb.applications.logistics.repo;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.hust.baseweb.applications.logistics.entity.UomType;

public interface UomTypeRepo extends PagingAndSortingRepository<UomType, String> {
	public UomType findByUomTypeId(String uomTypeId);
}
