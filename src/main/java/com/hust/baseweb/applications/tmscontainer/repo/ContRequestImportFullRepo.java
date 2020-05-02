package com.hust.baseweb.applications.tmscontainer.repo;

import com.hust.baseweb.applications.tmscontainer.entity.ContRequestImportFull;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface ContRequestImportFullRepo extends CrudRepository<ContRequestImportFull, UUID> {
    List<ContRequestImportFull> findAll();
    ContRequestImportFull findByRequestImportFullId(UUID id);
}
