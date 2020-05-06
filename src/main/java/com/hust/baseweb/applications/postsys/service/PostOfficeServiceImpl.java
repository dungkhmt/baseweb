package com.hust.baseweb.applications.postsys.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hust.baseweb.applications.customer.repo.CustomerRepo;
import com.hust.baseweb.applications.customer.repo.PartyContactMechPurposeRepo;
import com.hust.baseweb.applications.geo.entity.GeoPoint;
import com.hust.baseweb.applications.geo.entity.PostalAddress;
import com.hust.baseweb.applications.geo.repo.GeoPointRepo;
import com.hust.baseweb.applications.geo.repo.PostalAddressRepo;
import com.hust.baseweb.applications.postsys.entity.PostOffice;
import com.hust.baseweb.applications.postsys.model.postoffice.CreatePostOfficeInputModel;
import com.hust.baseweb.applications.postsys.repo.PostOfficeRepo;
import com.hust.baseweb.entity.Party;
import com.hust.baseweb.entity.PartyType;
import com.hust.baseweb.entity.Status.StatusEnum;
import com.hust.baseweb.repo.PartyRepo;
import com.hust.baseweb.repo.PartyTypeRepo;
import com.hust.baseweb.repo.StatusRepo;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PostOfficeServiceImpl implements PostOfficeService {

	private PostOfficeRepo postOfficeRepo;
	private StatusRepo statusRepo;
	private PartyRepo partyRepo;
	private GeoPointRepo geoPointRepo;
	private PostalAddressRepo postalAddressRepo;

	@Override
	public PostOffice save(CreatePostOfficeInputModel input) {
		// TODO Auto-generated method stub
		
		// create and save new party
		
		// create and save a new geo point
		GeoPoint geoPoint = new GeoPoint();
		geoPoint.setLatitude(input.getLatitude());
		geoPoint.setLongitude(input.getLongitude());
		geoPointRepo.save(geoPoint);
		log.info("save geo point, id=" + geoPoint.getGeoPointId());

		// create and save a new postal address
		PostalAddress postalAddress = new PostalAddress();
		postalAddress.setAddress(input.getAddress());
		postalAddress.setGeoPoint(geoPoint);
		postalAddressRepo.save(postalAddress);
		log.info("save postal address, contact_mech_id=" + postalAddress.getContactMechId());

		// create and save a new post office
		PostOffice postOffice = new PostOffice();
		postOffice.setPostOfficeId(input.getPostOfficeId());
		postOffice.setPostOfficeName(input.getPostOfficeName());
		postOffice.setPostOfficeLevel(input.getPostOfficeLevel());
		postOffice.setPostalAddress(postalAddress);
		postOfficeRepo.save(postOffice);
		log.info("save post office, post office id = " + postOffice.getPostOfficeId());

		return postOffice;
	}

	@Override
	public List<PostOffice> findAll() {
		// TODO Auto-generated method stub
		return postOfficeRepo.findAll();
	}

	@Override
	public PostOffice findByPostOfficeId(String postOfficeId) {
		// TODO Auto-generated method stub
		return postOfficeRepo.findByPostOfficeId(postOfficeId);
	}

	@Override
	public void deleteByPostOfficeId(String postOfficeId) {
		// TODO Auto-generated method stub
		PostOffice postOffice = postOfficeRepo.findByPostOfficeId(postOfficeId);
		if (postOffice != null) {
			postOfficeRepo.delete(postOffice);
		}
		
	}
}
