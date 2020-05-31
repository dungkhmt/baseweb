package com.hust.baseweb.applications.tmscontainer.repo;

import com.hust.baseweb.applications.tmscontainer.entity.ContContainer;
import com.hust.baseweb.applications.tmscontainer.entity.ContTrailer;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ContTrailerRepo extends CrudRepository<ContTrailer, String> {

    List<ContTrailer> findAll();

    ContContainer findByTrailerId(String id);
}
