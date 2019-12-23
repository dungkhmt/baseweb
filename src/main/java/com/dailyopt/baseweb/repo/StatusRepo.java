package com.dailyopt.baseweb.repo;

import com.dailyopt.baseweb.entity.Status;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "status", path = "status")
public interface StatusRepo extends CrudRepository<Status,String> {
}
