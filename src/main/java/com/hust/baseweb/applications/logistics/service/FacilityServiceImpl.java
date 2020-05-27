package com.hust.baseweb.applications.logistics.service;

import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.hust.baseweb.applications.geo.entity.GeoPoint;
import com.hust.baseweb.applications.geo.entity.PostalAddress;
import com.hust.baseweb.applications.geo.repo.GeoPointRepo;
import com.hust.baseweb.applications.geo.repo.PostalAddressRepo;
import com.hust.baseweb.applications.logistics.entity.Facility;
import com.hust.baseweb.applications.logistics.model.FacilityModel;
import com.hust.baseweb.applications.logistics.repo.FacilityRepo;
import com.hust.baseweb.utils.GoogleMapUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class FacilityServiceImpl implements FacilityService {

    private FacilityRepo facilityRepo;

    private PostalAddressRepo postalAddressRepo;
    private GeoPointRepo geoPointRepo;

    @Override
    public Facility findFacilityById(String facilityId) {

        return facilityRepo.findByFacilityId(facilityId);
    }

    @Override
    public List<Facility> getAllFacilities() {

        return facilityRepo.findAll();
    }

    @Override
    public Facility save(FacilityModel facilityModel) {

        GeocodingResult[] geocodingResults = GoogleMapUtils.queryLatLng(facilityModel.getAddress());
        PostalAddress postalAddress;
        if (geocodingResults != null && geocodingResults.length > 0) {
            LatLng location = geocodingResults[0].geometry.location;
            GeoPoint geoPoint = new GeoPoint(null, location.lat, location.lng);
            geoPoint = geoPointRepo.save(geoPoint);
            postalAddress = new PostalAddress(
                null,
                UUID.randomUUID().toString(),
                facilityModel.getAddress(),
                geoPoint,
                Double.MAX_VALUE);
            postalAddress = postalAddressRepo.save(postalAddress);
        } else {
            throw new RuntimeException("Address lat lng not found");
        }
        Facility facility = new Facility();
        facility.setFacilityId(facilityModel.getFacilityId());
        facility.setFacilityName(facilityModel.getFacilityName());
        facility.setPostalAddress(postalAddress);
        return facilityRepo.save(facility);
    }

    @Override
    public List<Facility> saveAll(List<FacilityModel> facilityModels) {

        return facilityModels.stream().map(this::save).collect(Collectors.toList());
    }

}
