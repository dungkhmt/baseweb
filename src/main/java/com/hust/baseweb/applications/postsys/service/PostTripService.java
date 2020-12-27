package com.hust.baseweb.applications.postsys.service;

import com.hust.baseweb.applications.postsys.entity.*;
import com.hust.baseweb.applications.postsys.model.ResponseSample;
import com.hust.baseweb.applications.postsys.model.posttrip.CreatePostTripModel;
import com.hust.baseweb.applications.postsys.model.posttrip.ExecuteTripInputModel;
import com.hust.baseweb.applications.postsys.model.posttrip.ExecuteTripOutputModel;
import com.hust.baseweb.applications.postsys.repo.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Log4j2
public class PostTripService {

    @Autowired
    PostFixedTripRepo postFixedTripRepo;

    @Autowired
    PostTripExecuteRepo postTripExecuteRepo;

    @Autowired
    PostOfficeTripRepo postOfficeTripRepo;

    @Autowired
    PostOrderRepo postOrderRepo;

    @Autowired
    PostShipOrderFixedTripPostOfficeAssignmentRepo postShipOrderFixedTripPostOfficeAssignmentRepo;

    public List<PostFixedTrip> findAllVehicle() {
        return postFixedTripRepo.findAll();
    }

    public PostFixedTrip findByPostOfficeFixedTripId(String postOfficeFixedTripId) {
        return postFixedTripRepo.findByPostOfficeFixedTripId(UUID.fromString(postOfficeFixedTripId));
    }


    public PostFixedTrip createPostTrip(CreatePostTripModel input) {
        PostOfficeTrip postOfficetrip = postOfficeTripRepo.findByFromPostOfficeIdAndToPostOfficeId(
            input.getFromPostOfficeId(),
            input.getToPostOfficeId());
        if (postOfficetrip == null) {
            postOfficetrip = new PostOfficeTrip();
            postOfficetrip.setFromPostOfficeId(input.getFromPostOfficeId());
            postOfficetrip.setToPostOfficeId(input.getToPostOfficeId());
            postOfficetrip.setFromDate(input.getFromDate());
            postOfficetrip.setThruDate(input.getThruDate());
            postOfficetrip = postOfficeTripRepo.save(postOfficetrip);
        }
        PostFixedTrip postFixedTrip = new PostFixedTrip();
        postFixedTrip.setPostOfficeTripId(postOfficetrip.getPostOfficeTripId());
        postFixedTrip.setFromDate(input.getFromDate());
        postFixedTrip.setThruDate(input.getThruDate());
        postFixedTrip.setScheduleDepartureTime(input.getSheduleDepatureTime());
        if (postFixedTrip.getFromDate() == null) {
            postFixedTrip.setFromDate(new Date());
        }
        return postFixedTripRepo.save(postFixedTrip);
    }

//
//    public void createPostTrip1(CreatePostTripModel input) {
//        PostOfficeTrip postOfficetrip = new PostOfficeTrip();
//        postOfficetrip.setFromPostOfficeId(input.getFromPostOfficeId());
//        postOfficetrip.setToPostOfficeId(input.getToPostOfficeId());
//        postOfficetrip.setFromDate(input.getFromDate());
//        postOfficetrip.setThruDate(input.getThruDate());
//        postOfficetrip = postOfficeTripRepo.save(postOfficetrip);
//    }

    public List<PostFixedTrip> createVehicleList(List<CreatePostTripModel> createPostTripModels) {
        List<PostFixedTrip> postFixedTrips = new ArrayList<>();
        for (CreatePostTripModel createPostTripModel : createPostTripModels) {
            PostOfficeTrip postOfficetrip = postOfficeTripRepo.findByFromPostOfficeIdAndToPostOfficeId(
                createPostTripModel.getFromPostOfficeId(),
                createPostTripModel.getToPostOfficeId());
            if (postOfficetrip == null) {
                postOfficetrip = new PostOfficeTrip();
                postOfficetrip.setFromPostOfficeId(createPostTripModel.getFromPostOfficeId());
                postOfficetrip.setToPostOfficeId(createPostTripModel.getToPostOfficeId());
                postOfficetrip.setFromDate(createPostTripModel.getFromDate());
                postOfficetrip.setThruDate(createPostTripModel.getThruDate());
                postOfficetrip = postOfficeTripRepo.save(postOfficetrip);
            }
            PostFixedTrip postFixedTrip = new PostFixedTrip();
            postFixedTrip.setPostOfficeTripId(postOfficetrip.getPostOfficeTripId());
            postFixedTrip.setFromDate(createPostTripModel.getFromDate());
            postFixedTrip.setThruDate(createPostTripModel.getThruDate());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            postFixedTrip.setScheduleDepartureTime(simpleDateFormat.format(createPostTripModel.getSheduleDepatureTime()));
            if (postFixedTrip.getFromDate() == null) {
                postFixedTrip.setFromDate(new Date());
            }
            postFixedTrips.add(postFixedTrip);
        }
        return postFixedTripRepo.saveAll(postFixedTrips);
    }

    public void deleteByPostTripId(String postTripId) {
        PostFixedTrip postFixedTrip = postFixedTripRepo.findByPostOfficeFixedTripId(UUID.fromString(postTripId));
        if (postFixedTrip != null) {
            postFixedTripRepo.delete(postFixedTrip);
        }
    }

    public ResponseSample createPostTripExecute(ExecuteTripInputModel executeTripInputModel) {
        PostFixedTrip postFixedTrip = postFixedTripRepo.findByPostOfficeFixedTripId(UUID.fromString(
            executeTripInputModel.getPostOfficeFixedTripId()));
        PostTripExecute postTripExecute = new PostTripExecute();
        postTripExecute.setPostOfficeFixedTripId(UUID.fromString(executeTripInputModel.getPostOfficeFixedTripId()));
        postTripExecute.setStatus("WAITING");
        postTripExecute = postTripExecuteRepo.save(postTripExecute);
        for (String postShipOrderFixedTripPostOfficeAssignmentId : executeTripInputModel.getPostShipOrderFixedTripPostOfficeAssignmentIds()) {
            PostShipOrderFixedTripPostOfficeAssignment postShipOrderFixedTripPostOfficeAssignment = new PostShipOrderFixedTripPostOfficeAssignment();
            postShipOrderFixedTripPostOfficeAssignment.setPostOfficeFixedTripExecuteId(postTripExecute.getPostOfficeFixedTripExecuteId());
            postShipOrderFixedTripPostOfficeAssignment.setPostShipOrderTripPostOfficeAssignmentId(UUID.fromString(
                postShipOrderFixedTripPostOfficeAssignmentId));
            postShipOrderFixedTripPostOfficeAssignmentRepo.save(postShipOrderFixedTripPostOfficeAssignment);
        }
        log.info("Create post trip execute success, postTripExecuteId: " +
                 postTripExecute.getPostOfficeFixedTripExecuteId() +
                 " fromPostOffice " +
                 postFixedTrip.getPostOfficetrip().getFromPostOfficeId() +
                 " -> toPostOffice: " +
                 postFixedTrip.getPostOfficetrip().getToPostOfficeId());
        return new ResponseSample("SUCCESS", "Execute Trip Success");
    }

    public ResponseSample updatePostTripExecute(ExecuteTripInputModel executeTripInputModel) {
        PostTripExecute postTripExecute = postTripExecuteRepo.findByPostOfficeFixedTripExecuteId(UUID.fromString(
            executeTripInputModel.getPostOfficeFixedTripExecuteId()));
        List<PostShipOrderFixedTripPostOfficeAssignment> postShipOrderFixedTripPostOfficeAssignments = postShipOrderFixedTripPostOfficeAssignmentRepo
            .findByPostOfficeFixedTripExecuteId(UUID.fromString(
                executeTripInputModel.getPostOfficeFixedTripExecuteId()));
        if (executeTripInputModel.getPostmanId() != null && executeTripInputModel.getPostmanId().length() > 0) {
            postTripExecute.setPostmanId(UUID.fromString(executeTripInputModel.getPostmanId()));
        }
        if (executeTripInputModel.getStatus() != null && executeTripInputModel.getStatus().length() > 0) {
            postTripExecute.setStatus(executeTripInputModel.getStatus());
        }
        if (executeTripInputModel.getDepatureDateTime() != null) {
            postTripExecute.setDepartureDateTime(executeTripInputModel.getDepatureDateTime());
        }
        if (executeTripInputModel.getStatus().equals("EXECUTING")) {
            List<PostOrder> postOrders = new ArrayList<>();
            for (PostShipOrderFixedTripPostOfficeAssignment postShipOrderFixedTripPostOfficeAssignment : postShipOrderFixedTripPostOfficeAssignments) {
                PostOrder postOrder = postShipOrderFixedTripPostOfficeAssignment
                    .getPostShipOrderTripPostOfficeAssignment()
                    .getPostOrder();
                postOrder.setStatusId("POST_ORDER_DELIVERING");
                postOrders.add(postOrder);
            }
            postOrderRepo.saveAll(postOrders);
        }
        postTripExecuteRepo.save(postTripExecute);
        return new ResponseSample("SUCCESS", "Update Trip Success");
    }

    public List<ExecuteTripOutputModel> getExecuteTripByDate(Date date) {
        List<ExecuteTripOutputModel> executeTripOutputModels = new ArrayList<>();
        return executeTripOutputModels;
    }
}
