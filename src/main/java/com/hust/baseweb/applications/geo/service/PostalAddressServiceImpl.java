package com.hust.baseweb.applications.geo.service;

import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hust.baseweb.applications.geo.entity.GeoPoint;
import com.hust.baseweb.applications.geo.entity.PostalAddress;
import com.hust.baseweb.applications.geo.repo.GeoPointRepo;
import com.hust.baseweb.applications.geo.repo.PostalAddressRepo;

@Service
@Log4j2
public class PostalAddressServiceImpl implements PostalAddressService {

	@Autowired
	private PostalAddressRepo postalAddressRepo;
	@Autowired
	private GeoPointRepo geoPointRepo;
	
	@Override
	public PostalAddress save(String address, String latitude, String longitude) {
		// TODO Auto-generated method stub
		GeoPoint geoPoint = new GeoPoint();
		geoPoint.setLatitude(latitude); geoPoint.setLongitude(longitude);
		geoPoint = geoPointRepo.save(geoPoint);
		PostalAddress postalAddress = new PostalAddress();
		postalAddress.setGeoPoint(geoPoint);
		postalAddress.setAddress(address);
		postalAddress = postalAddressRepo.save(postalAddress);
		return postalAddress;
	}

}
