package com.hust.baseweb.applications.sales.repo;

import com.hust.baseweb.applications.sales.entity.PartySalesman;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.UUID;

public interface PartySalesmanRepo extends PagingAndSortingRepository<PartySalesman, UUID> {

    PartySalesman findByPartyId(UUID partyId);

    List<PartySalesman> findAll();
}
