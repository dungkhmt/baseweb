package com.hust.baseweb.applications.customer.repo;

import com.hust.baseweb.applications.customer.entity.PartyRetailOutlet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PartyRetailOutletRepo extends JpaRepository<PartyRetailOutlet, UUID> {
    List<PartyRetailOutlet> findAll();
}
