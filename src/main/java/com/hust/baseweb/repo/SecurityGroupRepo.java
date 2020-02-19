package com.hust.baseweb.repo;

import com.hust.baseweb.entity.SecurityGroup;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface SecurityGroupRepo extends CrudRepository<SecurityGroup, String> {
    List<SecurityGroup> findAll();
}
