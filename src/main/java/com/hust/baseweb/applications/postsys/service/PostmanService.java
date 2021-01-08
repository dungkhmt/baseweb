package com.hust.baseweb.applications.postsys.service;

import com.hust.baseweb.applications.postsys.entity.PostOffice;
import com.hust.baseweb.applications.postsys.entity.PostOrder;
import com.hust.baseweb.applications.postsys.entity.PostShipOrderPostmanLastMileAssignment;
import com.hust.baseweb.applications.postsys.entity.Postman;
import com.hust.baseweb.applications.postsys.model.ResponseSample;
import com.hust.baseweb.applications.postsys.model.postman.PostmanAssignInput;
import com.hust.baseweb.applications.postsys.model.postman.PostmanAssignmentByDate;
import com.hust.baseweb.applications.postsys.model.postman.PostmanUpdateInputModel;
import com.hust.baseweb.applications.postsys.model.postman.PostmanUpdateOutputModel;
import com.hust.baseweb.applications.postsys.repo.*;
import com.hust.baseweb.repo.UserLoginRepo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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


    public List<PostShipOrderPostmanLastMileAssignment> findOrdersByPostmanAndDate(
        Principal principal,
        Date startDate,
        Date endDate
    ) {
        UUID postmanId = userLoginRepo.findByUserLoginId(principal.getName()).getParty().getPartyId();
        List<PostShipOrderPostmanLastMileAssignment> postShipOrderPostmanLastMileAssignments = postShipOrderPostmanLastMileAssignmentRepo
            .findByCreatedStampGreaterThanEqualAndCreatedStampLessThanAndPostmanId(startDate,
               new Date(endDate.getTime() + (1000 * 60 * 60 * 24)), postmanId);
        log.info("Get order assignment by postman: " +
                 startDate +
                 " -> " +
                 new Date(endDate.getTime() + (1000 * 60 * 60 * 24)) +
                 ": found " +
                 postShipOrderPostmanLastMileAssignments.size() +
                 " records");
        return postShipOrderPostmanLastMileAssignments;
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
}
