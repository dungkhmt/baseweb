package com.hust.baseweb.applications.tmscontainer.repo;

import com.hust.baseweb.applications.tmscontainer.entity.ContRequestImportEmpty;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface ContRequestImportEmptyRepo extends CrudRepository<ContRequestImportEmpty, UUID> {
    ContRequestImportEmpty findByRequestImportEmptyId(UUID id);

    List<ContRequestImportEmpty> findAll();

}
