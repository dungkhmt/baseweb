package com.hust.baseweb.applications.order.repo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hust.baseweb.applications.order.entity.PartyCustomer;

public interface PartyCustomerRepo extends JpaRepository<PartyCustomer, UUID>{
	
}
