package com.hust.baseweb.applications.postsys.service;

import com.hust.baseweb.applications.postsys.entity.PostOrder;
import com.hust.baseweb.applications.postsys.entity.PostShipOrderPostmanLastMileAssignment;
import com.hust.baseweb.applications.postsys.entity.Postman;
import com.hust.baseweb.applications.postsys.model.ResponseSample;
import com.hust.baseweb.applications.postsys.model.postman.PostmanAssignInput;
import com.hust.baseweb.applications.postsys.model.postman.PostmanAssignmentByDate;
import com.hust.baseweb.applications.postsys.repo.PostOrderRepo;
import com.hust.baseweb.applications.postsys.repo.PostShipOrderPostmanLastMileAssignmentRepo;
import com.hust.baseweb.applications.postsys.repo.PostmanRepo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public List<Postman> findByPostOfficeId(String postOfficeId) {
        return postmanRepo.findByPostOfficeId(postOfficeId);
    }

    public List<Postman> findOrdersByPostOfficeId(String postOfficeId) {
        return postmanRepo.findByPostOfficeId(postOfficeId);
    }

    public List<PostmanAssignmentByDate> findOrdersByPostOfficeIdAndDate(String postOfficeId, Date startDate, Date endDate) {
        List<PostShipOrderPostmanLastMileAssignment> postShipOrderPostmanLastMileAssignments = postShipOrderPostmanLastMileAssignmentRepo
            .findAllByCreatedStampGreaterThanEqualAndCreatedStampLessThan(startDate, new Date(endDate.getTime() + (1000 * 60 * 60 * 24)));
        log.info(startDate + " -> " + endDate + ": found " +  postShipOrderPostmanLastMileAssignments.size() + " results");
        List<PostmanAssignmentByDate> postmanAssignmentByDates = new ArrayList<>();
        List<Postman> postmen =  postmanRepo.findByPostOfficeId(postOfficeId);
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
        log.info("Successfully create " + + postShipOrderPostmanLastMileAssignments.size() +"  postman - postorder assignment");
        return new ResponseSample("SUCCESS", "Tạo mới thành công");
    }
}
