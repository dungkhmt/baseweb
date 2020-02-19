package com.hust.baseweb.applications.geo.repo;

import com.hust.baseweb.applications.geo.entity.GeoPoint;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface GeoPointRepo extends PagingAndSortingRepository<GeoPoint, UUID> {

}
