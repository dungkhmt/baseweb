package com.hust.baseweb.applications.tms.repo;

import com.hust.baseweb.applications.tms.entity.ContDepotTrailer;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ContDepotTrailerRepo extends CrudRepository<ContDepotTrailer, String> {
    List<ContDepotTrailer> findAll();
    ContDepotTrailer findByDepotTrailerId(String id);
}
