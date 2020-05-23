package com.hust.baseweb.applications.tmscontainer.repo;

import com.hust.baseweb.applications.tmscontainer.entity.ContContainerType;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ContContainerTypeRepo extends CrudRepository<ContContainerType, String> {
    List<ContContainerType> findAll();

    ContContainerType findByContainerTypeId(String containerTypeId);
}
