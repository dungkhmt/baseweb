package com.hust.baseweb.applications.tmscontainer.repo;

import com.hust.baseweb.applications.tmscontainer.entity.ContPort;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ContPortRepo extends CrudRepository<ContPort, String> {
    List<ContPort> findAll();

    ContPort findByPortId(String id);
}
