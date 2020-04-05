package com.hust.baseweb.applications.geo.repo;

import com.hust.baseweb.applications.geo.entity.Enumeration;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EnumerationRepo extends CrudRepository<Enumeration, String> {
    List<Enumeration> findAll();
    Enumeration findByEnumId(String enumId);
    List<Enumeration> findByEnumTypeId(String enumTypeId);
}
