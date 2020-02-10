package com.hust.baseweb.repo;

import com.hust.baseweb.entity.Status;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(collectionResourceRel = "status", path = "status")
public interface StatusRepo extends CrudRepository<Status, String> {

    @Override
    @RestResource(exported = false)
    public Status save(Status s);

    @Override
    @RestResource(exported = false)
    public void delete(Status s);
}
