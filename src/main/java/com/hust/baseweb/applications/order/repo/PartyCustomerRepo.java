package com.hust.baseweb.applications.order.repo;

import com.hust.baseweb.applications.customer.entity.PartyCustomer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;


public interface PartyCustomerRepo extends JpaRepository<PartyCustomer, UUID> {
    List<PartyCustomer> findAllByCustomerCodeIn(List<String> customerCodes);
    PartyCustomer findByPartyId(UUID partyId);

    List<PartyCustomer> findAllByCustomerCode(String customerCode);
}
