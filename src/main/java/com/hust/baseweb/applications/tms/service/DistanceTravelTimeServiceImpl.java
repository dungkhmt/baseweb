package com.hust.baseweb.applications.tms.service;

import com.hust.baseweb.applications.geo.entity.GeoPoint;
import com.hust.baseweb.applications.geo.repo.GeoPointRepo;
import com.hust.baseweb.applications.tms.entity.DistanceTravelTimeGeoPoint;
import com.hust.baseweb.applications.tms.repo.DistanceTravelTimeGeoPointRepo;
import com.hust.baseweb.utils.LatLngUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class DistanceTravelTimeServiceImpl implements DistanceTravelTimeService {

    private GeoPointRepo geoPointRepo;
    private DistanceTravelTimeGeoPointRepo distanceTravelTimeGeoPointRepo;

    @Override
    @Transactional
    public int calcAll() {
        List<GeoPoint> geoPoints = new ArrayList<>();
        geoPointRepo.findAll().forEach(geoPoints::add);

        List<DistanceTravelTimeGeoPoint> distanceTravelTimeGeoPoints = new ArrayList<>();

        for (GeoPoint fromGeoPoint : geoPoints) {
            for (GeoPoint toGeoPoint : geoPoints) {
            	if(fromGeoPoint.getLatitude() == null || fromGeoPoint.getLongitude() == null ||
            			toGeoPoint.getLatitude() == null || toGeoPoint.getLongitude() == null)
            		continue;
            	
                double latFrom = Double.parseDouble(fromGeoPoint.getLatitude());
                double lngFrom = Double.parseDouble(fromGeoPoint.getLongitude());
                double latTo = Double.parseDouble(toGeoPoint.getLatitude());
                double lngTo = Double.parseDouble(toGeoPoint.getLongitude());
                double distance = LatLngUtils.distance(latFrom, lngFrom, latTo, lngTo) * 1000; // meter
                double time = distance / 30_000 * 3600; // second (30km/h)
                DistanceTravelTimeGeoPoint distanceTravelTimeGeoPoint = new DistanceTravelTimeGeoPoint();
                distanceTravelTimeGeoPoint.setFromGeoPoint(fromGeoPoint);
                distanceTravelTimeGeoPoint.setToGeoPoint(toGeoPoint);
                distanceTravelTimeGeoPoint.setDistance(distance);
                distanceTravelTimeGeoPoint.setTravelTime(time);

                distanceTravelTimeGeoPoints.add(distanceTravelTimeGeoPoint);
            }
        }

        distanceTravelTimeGeoPointRepo.saveAll(distanceTravelTimeGeoPoints);
        return distanceTravelTimeGeoPoints.size();
    }
}
