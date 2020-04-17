package com.hust.baseweb.applications.tmscontainer.repo;

import com.hust.baseweb.applications.tmscontainer.entity.ContDepotContainer;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ContDepotContainerRepo extends CrudRepository<ContDepotContainer, String> {
    List<ContDepotContainer> findAll();
    ContDepotContainer findByDepotContainerId(String id);
}
