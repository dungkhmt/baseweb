package com.hust.baseweb.applications.geo.repo;

import com.hust.baseweb.applications.geo.entity.GeoPoint;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface GeoPointRepo extends PagingAndSortingRepository<GeoPoint, UUID> {

}
