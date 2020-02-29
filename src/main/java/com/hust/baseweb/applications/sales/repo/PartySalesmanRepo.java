package com.hust.baseweb.applications.sales.repo;

import java.util.UUID;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.hust.baseweb.applications.sales.entity.PartySalesman;
import java.util.List;
public interface PartySalesmanRepo extends PagingAndSortingRepository<PartySalesman, UUID> {
	public PartySalesman findByPartyId(UUID partyId);
	public List<PartySalesman> findAll();
}
