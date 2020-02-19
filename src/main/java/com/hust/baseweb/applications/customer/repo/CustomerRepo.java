package com.hust.baseweb.applications.customer.repo;

import com.hust.baseweb.applications.customer.entity.PartyCustomer;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.UUID;

public interface CustomerRepo extends
        PagingAndSortingRepository<PartyCustomer, UUID> {
    //JpaRepository<PartyCustomer, UUID> {
    public List<PartyCustomer> findAll();
}
