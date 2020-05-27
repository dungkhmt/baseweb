package com.hust.baseweb.applications.tmscontainer.repo;

import com.hust.baseweb.applications.tmscontainer.entity.ContRequestExportEmpty;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface ContRequestExportEmptyRepo extends CrudRepository<ContRequestExportEmpty, UUID> {

    ContRequestExportEmpty findByRequestExportEmptyId(UUID id);

    List<ContRequestExportEmpty> findAll();

}
