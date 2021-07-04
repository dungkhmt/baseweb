package com.hust.baseweb.applications.postsys.service;

import com.hust.baseweb.applications.postsys.entity.PostDriver;
import com.hust.baseweb.applications.postsys.entity.PostDriverPostOfficeAssignment;
import com.hust.baseweb.applications.postsys.entity.PostFixedTrip;
import com.hust.baseweb.applications.postsys.model.ResponseSample;
import com.hust.baseweb.applications.postsys.model.postdriver.PostDriverUpdateInputModel;
import com.hust.baseweb.applications.postsys.model.postdriver.UpdatePostDriverPostOfficeAssignmentInputModel;
import com.hust.baseweb.applications.postsys.model.postdriver.UpdatePostDriverPostOfficeAssignmentOutputModel;
import com.hust.baseweb.applications.postsys.repo.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Log4j2
public class PostDriverService {

    @Autowired
    PostShipOrderPostmanLastMileAssignmentRepo postShipOrderPostmanLastMileAssignmentRepo;

    @Autowired
    PostOrderRepo postOrderRepo;

    @Autowired
    private PostDriverRepo postDriverRepo;

    @Autowired
    private PostOfficeRepo postOfficeRepo;

    @Autowired
    private PostDriverPostOfficeAssignmentRepo postDriverPostOfficeAssignmentRepo;

    @Autowired
    private PostFixedTripRepo postFixedTripRepo;

    public List<PostDriver> findAll() {
        return postDriverRepo.findAll();
    }

    public ResponseSample updatePostDriver(PostDriverUpdateInputModel postDriverUpdateInputModel) {
        PostDriver postDriver = postDriverRepo.findByPostDriverId(UUID.fromString(postDriverUpdateInputModel.getPostDriverId()));
        postDriver.setPostDriverName(postDriverUpdateInputModel.getPostDriverName());
        postDriverRepo.save(postDriver);
        return new ResponseSample("SUCCESS", "Cập nhật thành công");
    }

    public UpdatePostDriverPostOfficeAssignmentOutputModel addPostDriverPostOfficeAssignment(
        UpdatePostDriverPostOfficeAssignmentInputModel updatePostDriverPostOfficeAssignmentInputModel
    ) {
        PostDriverPostOfficeAssignment postDriverPostOfficeAssignment = new PostDriverPostOfficeAssignment();
        PostDriver postDriver = postDriverRepo.findByPostDriverId(UUID.fromString(
            updatePostDriverPostOfficeAssignmentInputModel.getPostDriverId()));
        PostFixedTrip postFixedTrip = postFixedTripRepo
            .findById(UUID.fromString(updatePostDriverPostOfficeAssignmentInputModel.getPostOfficeFixedTripId()))
            .get();
        postDriverPostOfficeAssignment.setPostOfficeFixedTripId(postFixedTrip.getPostOfficeFixedTripId());
        postDriverPostOfficeAssignment.setPostFixedTrip(postFixedTrip);
        postDriverPostOfficeAssignment.setPostDriver(postDriver);
        postDriverPostOfficeAssignment.setPostDriverId(postDriver.getPostDriverId());
        postDriverPostOfficeAssignmentRepo.save(postDriverPostOfficeAssignment);
        return new UpdatePostDriverPostOfficeAssignmentOutputModel(
            "SUCCESS",
            "Cập nhật thành công",
            postDriverPostOfficeAssignment);
    }

    public UpdatePostDriverPostOfficeAssignmentOutputModel updatePostDriverPostOfficeAssignment(
        UpdatePostDriverPostOfficeAssignmentInputModel updatePostDriverPostOfficeAssignmentInputModel
    ) {
        PostDriverPostOfficeAssignment postDriverPostOfficeAssignment = postDriverPostOfficeAssignmentRepo
            .findById(UUID.fromString(updatePostDriverPostOfficeAssignmentInputModel.getPostDriverPostOfficeAssignmentId()))
            .get();
        PostFixedTrip postFixedTrip = postFixedTripRepo
            .findById(UUID.fromString(updatePostDriverPostOfficeAssignmentInputModel.getPostOfficeFixedTripId()))
            .get();
        postDriverPostOfficeAssignment.setPostOfficeFixedTripId(postFixedTrip.getPostOfficeFixedTripId());
        postDriverPostOfficeAssignment.setPostFixedTrip(postFixedTrip);
        postDriverPostOfficeAssignmentRepo.save(postDriverPostOfficeAssignment);
        return new UpdatePostDriverPostOfficeAssignmentOutputModel(
            "SUCCESS",
            "Cập nhật thành công",
            postDriverPostOfficeAssignment);
    }

    public ResponseSample deletePostDriverPostOfficeAssignment(UpdatePostDriverPostOfficeAssignmentInputModel updatePostDriverPostOfficeAssignmentInputModel) {
        PostDriverPostOfficeAssignment postDriverPostOfficeAssignment = postDriverPostOfficeAssignmentRepo
            .findById(UUID.fromString(updatePostDriverPostOfficeAssignmentInputModel.getPostDriverPostOfficeAssignmentId()))
            .get();
        postDriverPostOfficeAssignmentRepo.delete(postDriverPostOfficeAssignment);
        return new ResponseSample("SUCCESS", "Xoá thành công");
    }
}
