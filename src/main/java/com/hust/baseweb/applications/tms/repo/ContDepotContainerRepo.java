package com.hust.baseweb.applications.tms.repo;

import com.hust.baseweb.applications.tms.entity.ContDepotContainer;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ContDepotContainerRepo extends CrudRepository<ContDepotContainer, String> {
    List<ContDepotContainer> findAll();
    ContDepotContainer findByDepotContainerId(String id);
}
