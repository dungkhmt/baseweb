package com.hust.baseweb.applications.tmscontainer.repo;

import com.hust.baseweb.applications.tmscontainer.entity.ContRequestExportFull;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface ContRequestExportFullRepo extends CrudRepository<ContRequestExportFull, UUID> {

    List<ContRequestExportFull> findAll();

    ContRequestExportFull findByRequestExportFullId(UUID id);
}
