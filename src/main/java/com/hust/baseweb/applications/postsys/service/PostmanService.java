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
import java.util.*;
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
        Date endDate
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
                    postmanAssignmentByDate.getPostOrders().add(postShipOrderPostmanLastMileAssignment.getPostOrder());
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
        List<PostShipOrderPostmanLastMileAssignment> pickAssignment = solveAssignmentTsp(
            postShipOrderPostmanLastMileAssignments,
            postOffice,
            true);
        List<PostShipOrderPostmanLastMileAssignment> shipAssignment = solveAssignmentTsp(
            postShipOrderPostmanLastMileAssignments,
            postOffice,
            false);
        List<PostShipOrderPostmanLastMileAssignment> finishedAssignment = postShipOrderPostmanLastMileAssignments
            .stream()
            .filter((a) -> !pickAssignment.contains(a) && !shipAssignment.contains(a)).collect(Collectors.toList());
        log.info("Get order assignment by postman: " +
                 startDate +
                 " -> " +
                 new Date(endDate.getTime() + (1000 * 60 * 60 * 24)) +
                 ": found " +
                 postShipOrderPostmanLastMileAssignments.size() +
                 " records");
        return new PostShipOrderPostmanLastMileAssignmentOutputModel(pickAssignment, shipAssignment, finishedAssignment );
    }

    /**
     * Sap xep assignment theo tsp
     *
     * @param postShipOrderPostmanLastMileAssignments
     * @param postOffice
     * @param pick                                    true: pickup, false: ship
     * @return
     */
    public List<PostShipOrderPostmanLastMileAssignment> solveAssignmentTsp(
        List<PostShipOrderPostmanLastMileAssignment> postShipOrderPostmanLastMileAssignments,
        PostOffice postOffice, boolean pick
    ) {
        List<GeoPoint> geoPoints = new ArrayList<>();
        geoPoints.add(postOffice.getPostalAddress().getGeoPoint());
        List<PostShipOrderPostmanLastMileAssignment> source = new ArrayList<>();
        List<PostShipOrderPostmanLastMileAssignment> result = new ArrayList<>();
        for (int i = 0; i < postShipOrderPostmanLastMileAssignments.size(); i++) {
            if (pick && postShipOrderPostmanLastMileAssignments.get(i).getStatusId()
                                                               .equals("POST_ORDER_ASSIGNMENT_PICKUP_WAITING")) {
                source.add(postShipOrderPostmanLastMileAssignments.get(i));
                geoPoints.add(postShipOrderPostmanLastMileAssignments
                                  .get(i)
                                  .getPostOrder()
                                  .getFromCustomer()
                                  .getPostalAddress()
                                  .getGeoPoint());
            } else if (!pick && postShipOrderPostmanLastMileAssignments.get(i).getStatusId()
                                                                       .equals("POST_ORDER_ASSIGNMENT_SHIP_WAITING")) {
                geoPoints.add(postShipOrderPostmanLastMileAssignments
                                  .get(i)
                                  .getPostOrder()
                                  .getToCustomer()
                                  .getPostalAddress()
                                  .getGeoPoint());
            }
        }
        if (source.size() < 2) return source;
        // solve tsp
        TspSolver tspSolver = new TspSolver(geoPoints, 0);
        List<Integer> solution = tspSolver.solve();
        for (int s : solution) {
            result.add(source.get(s));
        }
        return result;
    }

    public ResponseSample createAssignment(List<PostmanAssignInput> postmanAssignInputs) {
        List<PostShipOrderPostmanLastMileAssignment> postShipOrderPostmanLastMileAssignments = new ArrayList<>();
        List<PostOrder> postOrders = new ArrayList<>();
        for (PostmanAssignInput postmanAssignInput : postmanAssignInputs) {
            for (String postOrderId : postmanAssignInput.getPostOrderIds()) {
                PostOrder postOrder = postOrderRepo.findById(UUID.fromString(postOrderId)).get();
                postOrder.setStatusId("POST_ORDER_READY_PICKUP"); //san sang doi postman pickup
                postOrders.add(postOrder);
                PostShipOrderPostmanLastMileAssignment postShipOrderPostmanLastMileAssignment = new PostShipOrderPostmanLastMileAssignment();
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
            PostOrder postOrder = postShipOrderPostmanLastMileAssignment.getPostOrder();
            postOrder.setStatusId("POST_ORDER_READY_FIND_PATH");
            postOrderRepo.save(postOrder);
        }
        return new ResponseSample("SUCCESS", "Update success");
    }
}
