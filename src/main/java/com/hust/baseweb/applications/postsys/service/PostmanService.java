package com.hust.baseweb.applications.postsys.service;

import com.hust.baseweb.applications.geo.entity.GeoPoint;
import com.hust.baseweb.applications.postsys.entity.PostOffice;
import com.hust.baseweb.applications.postsys.entity.PostOrder;
import com.hust.baseweb.applications.postsys.entity.PostShipOrderPostmanLastMileAssignment;
import com.hust.baseweb.applications.postsys.entity.Postman;
import com.hust.baseweb.applications.postsys.model.ResponseSample;
import com.hust.baseweb.applications.postsys.model.postman.*;
import com.hust.baseweb.applications.postsys.repo.*;
import com.hust.baseweb.applications.postsys.system.TspSolver;
import com.hust.baseweb.repo.UserLoginRepo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Log4j2
public class PostmanService {

    @Autowired
    PostmanRepo postmanRepo;
    @Autowired
    PostShipOrderPostmanLastMileAssignmentRepo postShipOrderPostmanLastMileAssignmentRepo;

    @Autowired
    PostOrderRepo postOrderRepo;

    @Autowired
    private PostDriverRepo postDriverRepo;

    @Autowired
    private PostOfficeRepo postOfficeRepo;

    @Autowired
    private UserLoginRepo userLoginRepo;


    public List<Postman> findAll() {
        return postmanRepo.findAll();
    }

    public List<Postman> findByPostOfficeId(String postOfficeId) {
        return postmanRepo.findByPostOfficeId(postOfficeId);
    }

    public List<Postman> findOrdersByPostOfficeId(String postOfficeId) {
        return postmanRepo.findByPostOfficeId(postOfficeId);
    }

    public List<PostmanAssignmentByDate> findOrdersByPostOfficeIdAndDate(
        String postOfficeId,
        Date startDate,
        Date endDate,
        boolean from
    ) {
        List<PostShipOrderPostmanLastMileAssignment> postShipOrderPostmanLastMileAssignments = postShipOrderPostmanLastMileAssignmentRepo
            .findAllByCreatedStampGreaterThanEqualAndCreatedStampLessThan(
                startDate,
                new Date(endDate.getTime() +
                         (1000 * 60 * 60 * 24)));
        log.info("Get Postman assignment: " +
                 startDate +
                 " -> " +
                 new Date(endDate.getTime() + (1000 * 60 * 60 * 24)) +
                 ": found " +
                 postShipOrderPostmanLastMileAssignments.size() +
                 " results");
        List<PostmanAssignmentByDate> postmanAssignmentByDates = new ArrayList<>();
        List<Postman> postmen = postmanRepo.findByPostOfficeId(postOfficeId);
        for (Postman postman : postmen) {
            PostmanAssignmentByDate postmanAssignmentByDate = new PostmanAssignmentByDate();
            postmanAssignmentByDate.setPostmanId(postman.getPostmanId());
            postmanAssignmentByDate.setPostmanName(postman.getPostmanName());
            postmanAssignmentByDate.setPostOfficeId(postman.getPostOfficeId());
            postmanAssignmentByDate.setPostOrders(new ArrayList<>());
            for (PostShipOrderPostmanLastMileAssignment postShipOrderPostmanLastMileAssignment : postShipOrderPostmanLastMileAssignments) {
                if (postman.getPostmanId().equals(postShipOrderPostmanLastMileAssignment.getPostmanId())) {
                    if (from) {
                        if (postShipOrderPostmanLastMileAssignment
                            .getStatusId()
                            .equals("POST_ORDER_ASSIGNMENT_PICKUP_WAITING")) {
                            postmanAssignmentByDate
                                .getPostOrders()
                                .add(postShipOrderPostmanLastMileAssignment.getPostOrder());
                        }
                    } else {
                        if (postShipOrderPostmanLastMileAssignment
                            .getStatusId()
                            .equals("POST_ORDER_ASSIGNMENT_SHIP_WAITING")) {
                            postmanAssignmentByDate
                                .getPostOrders()
                                .add(postShipOrderPostmanLastMileAssignment.getPostOrder());
                        }
                    }
                }
            }
            postmanAssignmentByDates.add(postmanAssignmentByDate);
        }
        return postmanAssignmentByDates;
    }


    public PostShipOrderPostmanLastMileAssignmentOutputModel findOrdersByPostmanAndDate(
        Principal principal,
        Date startDate,
        Date endDate
    ) {
        UUID postmanId = userLoginRepo.findByUserLoginId(principal.getName()).getParty().getPartyId();
        List<PostShipOrderPostmanLastMileAssignment> postShipOrderPostmanLastMileAssignments = postShipOrderPostmanLastMileAssignmentRepo
            .findByCreatedStampGreaterThanEqualAndCreatedStampLessThanAndPostmanId(
                startDate,
                new Date(endDate.getTime() +
                         (1000 * 60 * 60 * 24)),
                postmanId);
        PostOffice postOffice = getPostOfficeByPostman(principal);
        List<PostShipOrderPostmanLastMileAssignment> pickAssignment = new ArrayList<>();
        List<PostShipOrderPostmanLastMileAssignment> shipAssignment = new ArrayList<>();
        List<PostShipOrderPostmanLastMileAssignment> finishedAssignment = new ArrayList<>();
        for (PostShipOrderPostmanLastMileAssignment postShipOrderPostmanLastMileAssignment : postShipOrderPostmanLastMileAssignments) {
            switch (postShipOrderPostmanLastMileAssignment.getStatusId()) {
                case "POST_ORDER_ASSIGNMENT_PICKUP_WAITING":
                    pickAssignment.add(postShipOrderPostmanLastMileAssignment);
                    break;
                case "POST_ORDER_ASSIGNMENT_SHIP_WAITING":
                    shipAssignment.add(postShipOrderPostmanLastMileAssignment);
                    break;
                default:
                    finishedAssignment.add(postShipOrderPostmanLastMileAssignment);
            }
        }
        log.info("Get order assignment by postman: " +
                 startDate +
                 " -> " +
                 new Date(endDate.getTime() + (1000 * 60 * 60 * 24)) +
                 ": found " +
                 postShipOrderPostmanLastMileAssignments.size() +
                 " records");
        return new PostShipOrderPostmanLastMileAssignmentOutputModel(
            pickAssignment,
            shipAssignment,
            finishedAssignment);
    }


    /**
     * Sap xep assignment theo tsp
     *
     * @param solvePostmanPostOrderAssignmentTspInputModel
     * @return
     */
    public List<PostShipOrderPostmanLastMileAssignment> solveAssignmentTsp(
        SolvePostmanPostOrderAssignmentTspInputModel solvePostmanPostOrderAssignmentTspInputModel
    ) {
        List<PostShipOrderPostmanLastMileAssignment> postShipOrderPostmanLastMileAssignments = postShipOrderPostmanLastMileAssignmentRepo
            .findByPostShipOrderPostmanLastMileAssignmentIdIn(solvePostmanPostOrderAssignmentTspInputModel
                                                                  .getPostShipOrderPostmanLastMileAssignmentIds()
                                                                  .stream()
                                                                  .map(postShipOrderPostmanLastMileAssignmentId -> UUID
                                                                      .fromString(
                                                                          postShipOrderPostmanLastMileAssignmentId))
                                                                  .collect(Collectors.toList()));
        PostOffice postOffice = postOfficeRepo.findByPostOfficeId(solvePostmanPostOrderAssignmentTspInputModel.getPostOfficeId());
        List<GeoPoint> geoPoints = new ArrayList<>();
        geoPoints.add(postOffice.getPostalAddress().getGeoPoint());
        List<PostShipOrderPostmanLastMileAssignment> source = new ArrayList<>();
        List<PostShipOrderPostmanLastMileAssignment> result = new ArrayList<>();
        for (int i = 0; i < postShipOrderPostmanLastMileAssignments.size(); i++) {
            if (solvePostmanPostOrderAssignmentTspInputModel.getPick() &&
                postShipOrderPostmanLastMileAssignments.get(i).getStatusId()
                                                       .equals("POST_ORDER_ASSIGNMENT_PICKUP_WAITING")) {
                source.add(postShipOrderPostmanLastMileAssignments.get(i));
                geoPoints.add(postShipOrderPostmanLastMileAssignments
                                  .get(i)
                                  .getPostOrder()
                                  .getFromCustomer()
                                  .getPostalAddress()
                                  .getGeoPoint());
            } else if (!solvePostmanPostOrderAssignmentTspInputModel.getPick() &&
                       postShipOrderPostmanLastMileAssignments.get(i).getStatusId()
                                                              .equals("POST_ORDER_ASSIGNMENT_SHIP_WAITING")) {
                geoPoints.add(postShipOrderPostmanLastMileAssignments
                                  .get(i)
                                  .getPostOrder()
                                  .getToCustomer()
                                  .getPostalAddress()
                                  .getGeoPoint());
            }
        }
        if (source.size() < 2) {
            return source;
        }
        // solve tsp
        TspSolver tspSolver = new TspSolver(geoPoints, 0);
        List<Integer> solution = tspSolver.solve();
        for (int s : solution) {
            result.add(source.get(s));
        }
        return result;
    }

    public ResponseSample createAssignment(List<PostmanAssignInput> postmanAssignInputs, boolean pick) {
        List<PostShipOrderPostmanLastMileAssignment> postShipOrderPostmanLastMileAssignments = new ArrayList<>();
        List<PostOrder> postOrders = new ArrayList<>();
        for (PostmanAssignInput postmanAssignInput : postmanAssignInputs) {
            for (String postOrderId : postmanAssignInput.getPostOrderIds()) {
                PostOrder postOrder = postOrderRepo.findById(UUID.fromString(postOrderId)).get();
                PostShipOrderPostmanLastMileAssignment postShipOrderPostmanLastMileAssignment = new PostShipOrderPostmanLastMileAssignment();
                if (pick) {
                    postOrder.setStatusId("POST_ORDER_READY_PICKUP"); //san sang doi postman pickup
                    postShipOrderPostmanLastMileAssignment.setStatusId("POST_ORDER_ASSIGNMENT_PICKUP_WAITING");
                    postShipOrderPostmanLastMileAssignment.setPickupDelivery("p");
                }
                else{
                    postOrder.setStatusId("POST_ORDER_READY_SHIP");
                    postShipOrderPostmanLastMileAssignment.setStatusId("POST_ORDER_ASSIGNMENT_SHIP_WAITING");
                    postShipOrderPostmanLastMileAssignment.setPickupDelivery("d");
                }
                postOrders.add(postOrder);
                postShipOrderPostmanLastMileAssignment.setPostmanId(UUID.fromString(postmanAssignInput.getPostmanId()));
                postShipOrderPostmanLastMileAssignment.setPostShipOrderId(UUID.fromString(postOrderId));
                postShipOrderPostmanLastMileAssignments.add(postShipOrderPostmanLastMileAssignment);
            }
        }
        postShipOrderPostmanLastMileAssignmentRepo.saveAll(postShipOrderPostmanLastMileAssignments);
        postOrderRepo.saveAll(postOrders);
        log.info("Successfully create " +
                 +postShipOrderPostmanLastMileAssignments.size() +
                 "  postman - postorder assignment");
        return new ResponseSample("SUCCESS", "Tạo mới thành công");
    }

    public PostmanUpdateOutputModel updatePostman(PostmanUpdateInputModel postmanUpdateInputModel) {
        Postman postman = postmanRepo.findByPostmanId(UUID.fromString(postmanUpdateInputModel.getPostmanId()));
        PostOffice postOffice = postOfficeRepo.findByPostOfficeId(postmanUpdateInputModel.getPostOfficeId());
        if (postOffice == null) {
            return new PostmanUpdateOutputModel("ERROR", "Không có mã bưu cục này", null);
        }
        postman.setPostmanName(postmanUpdateInputModel.getPostmanName());
        postman.setPostOfficeId(postmanUpdateInputModel.getPostOfficeId());
        postman.setPostOffice(postOffice);
        postmanRepo.save(postman);
        return new PostmanUpdateOutputModel("SUCCESS", "Cập nhật thành công", postman);
    }

    public PostOffice getPostOfficeByPostman(Principal principal) {
        UUID postmanId = userLoginRepo.findByUserLoginId(principal.getName()).getParty().getPartyId();
        return postmanRepo.findByPostmanId(postmanId).getPostOffice();
    }

    public ResponseSample updatePostOrderAssignment(UpdatePostmanPostOrderAssignmentInputModel updatePostmanPostOrderAssignmentInputModel) {
        PostShipOrderPostmanLastMileAssignment postShipOrderPostmanLastMileAssignment = postShipOrderPostmanLastMileAssignmentRepo
            .findById(UUID.fromString(updatePostmanPostOrderAssignmentInputModel.getPostShipOrderPostmanLastMileAssignmentId()))
            .get();
        if (!updatePostmanPostOrderAssignmentInputModel
            .getStatus()
            .equals(postShipOrderPostmanLastMileAssignment.getStatusId())) {
            postShipOrderPostmanLastMileAssignment.setStatusId(updatePostmanPostOrderAssignmentInputModel.getStatus());
            if (updatePostmanPostOrderAssignmentInputModel.getStatus().equals("POST_ORDER_ASSIGNMENT_PICKUP_SUCCESS")) {
                PostOrder postOrder = postShipOrderPostmanLastMileAssignment.getPostOrder();
                postOrder.setStatusId("POST_ORDER_READY_FIND_PATH");
                postOrder.setCurrentPostOfficeId(postOrder.getFromPostOfficeId());
                postOrderRepo.save(postOrder);
            }
            if (updatePostmanPostOrderAssignmentInputModel.getStatus().equals("POST_ORDER_ASSIGNMENT_SHIP_SUCCESS")) {
                PostOrder postOrder = postShipOrderPostmanLastMileAssignment.getPostOrder();
                postOrder.setStatusId("POST_ORDER_SUCCESS");
                postOrder.setCurrentPostOfficeId(postOrder.getFromPostOfficeId());
                postOrderRepo.save(postOrder);
            }
        }
        return new ResponseSample("SUCCESS", "Update success");
    }
}
