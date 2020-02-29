package com.hust.baseweb.applications.tms.repo;

import com.hust.baseweb.applications.tms.entity.DistanceTravelTimeGeoPoint;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
public interface DistanceTravelTimeGeoPointRepo extends PagingAndSortingRepository<DistanceTravelTimeGeoPoint, String> {

}
