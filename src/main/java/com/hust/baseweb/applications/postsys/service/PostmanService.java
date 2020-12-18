package com.hust.baseweb.applications.postsys.service;

import com.hust.baseweb.applications.postsys.entity.PostShipOrderPostmanLastMileAssignment;
import com.hust.baseweb.applications.postsys.entity.Postman;
import com.hust.baseweb.applications.postsys.model.ResponseSample;
import com.hust.baseweb.applications.postsys.model.postman.PostmanAssignInput;
import com.hust.baseweb.applications.postsys.repo.PostShipOrderPostmanLastMileAssignmentRepo;
import com.hust.baseweb.applications.postsys.repo.PostmanRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PostmanService {

    @Autowired
    PostmanRepo postmanRepo;
    @Autowired
    PostShipOrderPostmanLastMileAssignmentRepo postShipOrderPostmanLastMileAssignmentRepo;

    public List<Postman> findByPostOfficeId(String postOfficeId) {
        return postmanRepo.findByPostOfficeId(postOfficeId);
    }

    public List<Postman> findOrdersByPostOfficeId(String postOfficeId) {
        List<PostShipOrderPostmanLastMileAssignment> postShipOrderPostmanLastMileAssignments = postShipOrderPostmanLastMileAssignmentRepo
            .findAll();
        return postmanRepo.findByPostOfficeId(postOfficeId);
    }

    public ResponseSample createAssignment(List<PostmanAssignInput> postmanAssignInputs) {
        for (PostmanAssignInput postmanAssignInput : postmanAssignInputs) {
            PostShipOrderPostmanLastMileAssignment postShipOrderPostmanLastMileAssignment = new PostShipOrderPostmanLastMileAssignment();
            postShipOrderPostmanLastMileAssignment.setPostmanId(UUID.fromString(postmanAssignInput.getPostmanId()));
            for (String postOrderId : postmanAssignInput.getPostOrderIds()) {
                postShipOrderPostmanLastMileAssignment.setPostShipOrderId(UUID.fromString(postOrderId));
            }
        }
        return new ResponseSample("SUCCESS", "Tạo mới thành công");
    }
}
