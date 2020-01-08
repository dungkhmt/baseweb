package com.hust.baseweb.repo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.hust.baseweb.entity.PartyType;

@RepositoryRestResource(exported = false)

public interface PartyTypeRepo extends JpaRepository<PartyType, String>{

}
