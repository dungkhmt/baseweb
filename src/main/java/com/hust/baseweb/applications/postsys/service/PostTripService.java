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
import java.util.stream.Collectors;

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

    @Autowired
    PostShipOrderTripPostOfficeAssignmentRepo postShipOrderTripPostOfficeAssignmentRepo;

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

    public ExecuteTripOutputModel createPostTripExecute(ExecuteTripInputModel executeTripInputModel) {
        PostFixedTrip postFixedTrip = postFixedTripRepo.findByPostOfficeFixedTripId(UUID.fromString(
            executeTripInputModel.getPostOfficeFixedTripId()));
        PostTripExecute postTripExecute = new PostTripExecute();
        postTripExecute.setPostOfficeFixedTripId(UUID.fromString(executeTripInputModel.getPostOfficeFixedTripId()));
        postTripExecute.setStatus("WAITING");
        postTripExecute = postTripExecuteRepo.save(postTripExecute);
        List<PostShipOrderFixedTripPostOfficeAssignment> postShipOrderFixedTripPostOfficeAssignments = new ArrayList<>();
        List<PostOrder> postOrders = new ArrayList<>();
        for (String postShipOrderTripPostOfficeAssignmentId : executeTripInputModel.getPostShipOrderTripPostOfficeAssignmentIds()) {
            PostShipOrderTripPostOfficeAssignment postShipOrderTripPostOfficeAssignment = postShipOrderTripPostOfficeAssignmentRepo
                .findById(
                    UUID.fromString(postShipOrderTripPostOfficeAssignmentId))
                .get();
            PostOrder postOrder = postShipOrderTripPostOfficeAssignment.getPostOrder();
            postOrder.setStatusId("POST_ORDER_WAIT_DELIVERY");
            postOrders.add(postOrder);
            PostShipOrderFixedTripPostOfficeAssignment postShipOrderFixedTripPostOfficeAssignment = new PostShipOrderFixedTripPostOfficeAssignment();
            postShipOrderFixedTripPostOfficeAssignment.setPostOfficeFixedTripExecuteId(postTripExecute.getPostOfficeFixedTripExecuteId());
            postShipOrderFixedTripPostOfficeAssignment.setPostShipOrderTripPostOfficeAssignmentId(UUID.fromString(
                postShipOrderTripPostOfficeAssignmentId));
            postShipOrderFixedTripPostOfficeAssignments.add(postShipOrderFixedTripPostOfficeAssignment);
        }
        postShipOrderFixedTripPostOfficeAssignments = postShipOrderFixedTripPostOfficeAssignmentRepo.saveAll(
            postShipOrderFixedTripPostOfficeAssignments);
        postOrderRepo.saveAll(postOrders);
        log.info("Create post trip execute success, postTripExecuteId: " +
                 postTripExecute.getPostOfficeFixedTripExecuteId() +
                 " fromPostOffice " +
                 postFixedTrip.getPostOfficeTrip().getFromPostOfficeId() +
                 " -> toPostOffice: " +
                 postFixedTrip.getPostOfficeTrip().getToPostOfficeId());
        return new ExecuteTripOutputModel(
            postTripExecute.getPostOfficeFixedTripExecuteId().toString(),
            executeTripInputModel.getPostOfficeFixedTripId(),
            null,
            null,
            executeTripInputModel.getStatus(),
            postShipOrderFixedTripPostOfficeAssignments);
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
        Date tomorrow = new Date(date.getTime() + (1000 * 60 * 60 * 24));
        List<PostTripExecute> postTripExecutes = postTripExecuteRepo.findAllByCreatedStampGreaterThanEqualAndCreatedStampLessThan(
            date,
            tomorrow);
        List<PostShipOrderFixedTripPostOfficeAssignment> postShipOrderFixedTripPostOfficeAssignments = postShipOrderFixedTripPostOfficeAssignmentRepo
            .findByPostOfficeFixedTripExecuteIdIn(
                postTripExecutes
                    .stream()
                    .map((postTripExecute) -> postTripExecute.getPostOfficeFixedTripExecuteId())
                    .collect(Collectors.toList())
            );
        List<ExecuteTripOutputModel> executeTripOutputModels = new ArrayList<>();
        for (PostTripExecute postTripExecute : postTripExecutes) {
            ExecuteTripOutputModel executeTripOutputModel = new ExecuteTripOutputModel();
            executeTripOutputModel.setPostOfficeFixedTripExecuteId(postTripExecute
                                                                       .getPostOfficeFixedTripExecuteId()
                                                                       .toString());
            executeTripOutputModel.setPostOfficeFixedTripId(postTripExecute.getPostOfficeFixedTripId().toString());
            executeTripOutputModel.setDepatureDateTime(postTripExecute.getDepartureDateTime());
            executeTripOutputModel.setPostmanName(postTripExecute.getPostman().getPostmanName());
            executeTripOutputModel.setStatus(postTripExecute.getStatus());
            executeTripOutputModel.setPostShipOrderFixedTripPostOfficeAssignments(new ArrayList<>());
            for (PostShipOrderFixedTripPostOfficeAssignment postShipOrderFixedTripPostOfficeAssignment : postShipOrderFixedTripPostOfficeAssignments) {
                if (postShipOrderFixedTripPostOfficeAssignment
                    .getPostOfficeFixedTripExecute()
                    .equals(postTripExecute.getPostOfficeFixedTripExecuteId())) {
                    executeTripOutputModel
                        .getPostShipOrderFixedTripPostOfficeAssignments()
                        .add(postShipOrderFixedTripPostOfficeAssignment);
                }
            }
            executeTripOutputModels.add(executeTripOutputModel);
        }
        log.info("Get execute trip list " + executeTripOutputModels.size() + " records found");
        return executeTripOutputModels;
    }

    /**
     * @return Lay danh sach don hang da duoc gan boi chuyen xe
     */
    public List<PostShipOrderTripPostOfficeAssignment> getPostOrderByTrip(Date fromDate, Date toDate) {
        Date tomorrow = new Date(toDate.getTime() + (1000 * 60 * 60 * 24));
        return postShipOrderTripPostOfficeAssignmentRepo.findByMaxDeliveryOrderAndDate(
            fromDate,
            tomorrow,
            "POST_ORDER_READY_DELIVERY");
    }
}
