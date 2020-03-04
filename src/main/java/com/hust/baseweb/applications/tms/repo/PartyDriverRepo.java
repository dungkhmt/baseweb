package com.hust.baseweb.applications.tms.repo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hust.baseweb.applications.tms.entity.PartyDriver;

public interface PartyDriverRepo extends JpaRepository<PartyDriver, UUID> {

}
