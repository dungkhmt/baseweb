package com.hust.baseweb.applications.postsys.service;

import com.hust.baseweb.applications.geo.entity.GeoPoint;
import com.hust.baseweb.applications.geo.entity.PostalAddress;
import com.hust.baseweb.applications.geo.repo.GeoPointRepo;
import com.hust.baseweb.applications.geo.repo.PostalAddressRepo;
import com.hust.baseweb.applications.postsys.entity.PostOffice;
import com.hust.baseweb.applications.postsys.entity.PostOrder;
import com.hust.baseweb.applications.postsys.model.postoffice.CreatePostOfficeInputModel;
import com.hust.baseweb.applications.postsys.model.postoffice.OfficeOrderDetailOutput;
import com.hust.baseweb.applications.postsys.model.postoffice.PostOfficeOrderStatusOutputModel;
import com.hust.baseweb.applications.postsys.repo.PostOfficeRepo;
import com.hust.baseweb.applications.postsys.repo.PostOrderRepo;
import com.hust.baseweb.repo.PartyRepo;
import com.hust.baseweb.repo.StatusRepo;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Log4j2
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PostOfficeServiceImpl implements PostOfficeService {

    private PostOfficeRepo postOfficeRepo;
    private StatusRepo statusRepo;
    private PartyRepo partyRepo;
    private GeoPointRepo geoPointRepo;
    private PostalAddressRepo postalAddressRepo;
    private PostOrderRepo postOrderRepo;

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

    @Override
    public List<PostOffice> save(List<CreatePostOfficeInputModel> inputs) {
        List<PostalAddress> postalAddresses = new ArrayList<>();
        List<GeoPoint> geoPoints = new ArrayList<>();
        List<PostOffice> postOffices = new ArrayList<>();
        inputs.forEach(input -> {
            GeoPoint geoPoint = new GeoPoint();
            geoPoint.setLatitude(input.getLatitude());
            geoPoint.setLongitude(input.getLongitude());
            geoPoints.add(geoPoint);

            // create and save a new postal address
            PostalAddress postalAddress = new PostalAddress();
            postalAddress.setAddress(input.getAddress());
            postalAddress.setGeoPoint(geoPoint);
            postalAddresses.add(postalAddress);

            PostOffice postOffice = new PostOffice();
            postOffice.setPostOfficeId(input.getPostOfficeId());
            postOffice.setPostOfficeName(input.getPostOfficeName());
            postOffice.setPostOfficeLevel(input.getPostOfficeLevel());
            postOffice.setPostalAddress(postalAddress);
            postOffices.add(postOffice);
        });
        geoPointRepo.saveAll(geoPoints);
        postalAddressRepo.saveAll(postalAddresses);
        postOfficeRepo.saveAll(postOffices);
        return postOffices;
    }

    @Override
    public OfficeOrderDetailOutput getOfficeOrderDetailOutput(String postOfficeId, Date startDate, Date endDate) {
        PostOffice postOffice = postOfficeRepo.findById(postOfficeId).get();
        Date tomorrow = new Date(endDate.getTime() + (1000 * 60 * 60 * 24));
        List<PostOrder> fromPostOrders = postOrderRepo.findByFromPostOfficeIdAndStatusIdAndCreatedStampGreaterThanEqualAndCreatedStampLessThan(
            postOfficeId,
            "POST_ORDER_ASSIGNED",
            startDate,
            tomorrow);
        List<PostOrder> toPostOrders = postOrderRepo.findByToPostOfficeIdAndStatusIdAndCreatedStampGreaterThanEqualAndCreatedStampLessThan(
            postOfficeId,
            "POST_ORDER_FINAL_TRIP",
            startDate,
            tomorrow);
        log.info("Get office pick order detail: " +
                 startDate +
                 " -> " +
                 endDate +
                 " found " +
                 fromPostOrders.size() +
                 " frompostOrders, " +
                 toPostOrders.size() +
                 " toPostOrders");
        return new OfficeOrderDetailOutput(postOffice, fromPostOrders, toPostOrders);
    }

    @Override
    public List<PostOfficeOrderStatusOutputModel> getPostOfficeOrderStatus(Date fromDate, Date toDate, boolean from) {
        List<PostOffice> postOffices = postOfficeRepo.findAll();
        List<PostOfficeOrderStatusOutputModel> postOfficeOrderStatusOutputModels = new ArrayList<>();
        for (PostOffice postOffice : postOffices) {
            PostOfficeOrderStatusOutputModel postOfficeOrderStatusOutputModel = new PostOfficeOrderStatusOutputModel(
                postOffice);
            List<PostOrder> postOrders = new ArrayList<>();
            if (from) {
                postOrders = postOrderRepo.findByFromPostOfficeIdAndStatusIdAndCreatedStampGreaterThanEqualAndCreatedStampLessThan(
                    postOffice.getPostOfficeId(), "POST_ORDER_ASSIGNED", fromDate, new Date(toDate.getTime() + (1000 * 60 * 60 * 24))
                );
            } else {
                postOrders = postOrderRepo.findByToPostOfficeIdAndStatusIdAndCreatedStampGreaterThanEqualAndCreatedStampLessThan(
                    postOffice.getPostOfficeId(), "POST_ORDER_FINAL_TRIP", fromDate, new Date(toDate.getTime() + (1000 * 60 * 60 * 24))
                );
            }
            if (postOrders.size() > 0) {
                postOfficeOrderStatusOutputModel.setStatus(true);
            }
            postOfficeOrderStatusOutputModels.add(postOfficeOrderStatusOutputModel);
        }
        return postOfficeOrderStatusOutputModels;
    }
}
