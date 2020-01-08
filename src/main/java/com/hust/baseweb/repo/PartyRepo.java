package com.hust.baseweb.repo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.hust.baseweb.entity.Party;

@RepositoryRestResource(exported = false)
public interface PartyRepo extends JpaRepository<Party, UUID>{

}
