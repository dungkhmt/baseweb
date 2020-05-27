package com.hust.baseweb.repo;

import com.hust.baseweb.entity.SecurityGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Set;

@RepositoryRestResource(exported = false)
public interface SecurityGroupRepo extends JpaRepository<SecurityGroup, String> {

    List<SecurityGroup> findAll();

    Set<SecurityGroup> findAllByGroupIdIn(List<String> groupIds);
}
