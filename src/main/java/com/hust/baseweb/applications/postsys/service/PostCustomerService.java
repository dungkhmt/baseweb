package com.hust.baseweb.applications.postsys.service;

import com.hust.baseweb.applications.geo.entity.GeoPoint;
import com.hust.baseweb.applications.geo.entity.PostalAddress;
import com.hust.baseweb.applications.geo.repo.GeoPointRepo;
import com.hust.baseweb.applications.geo.repo.PostalAddressRepo;
import com.hust.baseweb.applications.postsys.entity.PostCustomer;
import com.hust.baseweb.applications.postsys.model.postcustomer.CreatePostCustomerModel;
import com.hust.baseweb.applications.postsys.repo.PostCustomerRepo;
import com.hust.baseweb.entity.UserLogin;
import com.hust.baseweb.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
@Log4j2
public class PostCustomerService {

    @Autowired
    PostCustomerRepo postCustomerRepo;

    @Autowired
    GeoPointRepo geoPointRepo;

    @Autowired
    PostalAddressRepo postalAddressRepo;

    @Autowired
    UserService userService;

    public List<PostCustomer> findAll() {
        return postCustomerRepo.findAll();
    }

    public PostCustomer saveCustomer(CreatePostCustomerModel input) {
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
        PostCustomer postCustomer = new PostCustomer();
        postCustomer.setPostCustomerName(input.getPostCustomerName());
        postCustomer.setPhoneNum(input.getPhoneNum());
        postCustomer.setPostalAddress(postalAddress);
        return postCustomerRepo.save(postCustomer);
    }

    public PostCustomer findCustomerByPartyId(Principal principal) {
        UserLogin userLogin = userService.findById(principal.getName());
        return postCustomerRepo.findByPartyId(userLogin.getParty().getPartyId());
    }
}
