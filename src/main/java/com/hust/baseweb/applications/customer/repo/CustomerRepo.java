package com.hust.baseweb.applications.customer.repo;

import java.util.UUID;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.hust.baseweb.applications.customer.entity.PartyCustomer;





public interface CustomerRepo extends
		PagingAndSortingRepository<PartyCustomer, UUID> {

}
