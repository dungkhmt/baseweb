package com.hust.baseweb.repo;

import java.util.List;

import com.hust.baseweb.entity.SecurityGroup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface SecurityGroupRepo extends JpaRepository<SecurityGroup, String> {
	List<SecurityGroup> findAll();
}
