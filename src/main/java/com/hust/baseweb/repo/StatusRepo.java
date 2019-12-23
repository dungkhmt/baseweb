package com.hust.baseweb.repo;

import com.hust.baseweb.entity.Status;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "status", path = "status")
public interface StatusRepo extends CrudRepository<Status,String> {
}
