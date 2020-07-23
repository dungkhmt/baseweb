package com.hust.baseweb.applications.gismap.repo;

import com.hust.baseweb.applications.gismap.document.Street;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface StreetRepo extends MongoRepository<Street, String> {
    List<Street> findAll();
}
