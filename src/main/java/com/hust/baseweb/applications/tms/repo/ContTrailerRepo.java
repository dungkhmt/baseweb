package com.hust.baseweb.applications.tms.repo;

import com.hust.baseweb.applications.tms.entity.ContContainer;
import com.hust.baseweb.applications.tms.entity.ContTrailer;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ContTrailerRepo extends CrudRepository<ContTrailer,String> {
    List<ContTrailer> findAll();
    ContContainer findByTrailerId(String id);
}
