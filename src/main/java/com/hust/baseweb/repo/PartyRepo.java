package com.hust.baseweb.repo;

import com.hust.baseweb.entity.Party;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.UUID;

@RepositoryRestResource(exported = false)
public interface PartyRepo extends JpaRepository<Party, UUID> {

}
