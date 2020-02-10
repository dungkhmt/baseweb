package com.hust.baseweb.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.hust.baseweb.entity.SecurityGroup;

@RepositoryRestResource(exported = false)
public interface SecurityGroupRepo extends CrudRepository<SecurityGroup, String> {
	List<SecurityGroup> findAll();
}
