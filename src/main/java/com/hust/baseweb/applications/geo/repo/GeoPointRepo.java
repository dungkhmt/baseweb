package com.hust.baseweb.applications.geo.repo;

import java.util.UUID;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.hust.baseweb.applications.geo.entity.GeoPoint;

public interface GeoPointRepo extends PagingAndSortingRepository<GeoPoint, UUID> {

}
