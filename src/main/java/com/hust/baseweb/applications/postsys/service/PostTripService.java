package com.hust.baseweb.applications.postsys.service;

import com.hust.baseweb.applications.postsys.entity.PostOffice;
import com.hust.baseweb.applications.postsys.entity.PostFixedTrip;
import com.hust.baseweb.applications.postsys.entity.PostTripExecute;
import com.hust.baseweb.applications.postsys.model.ResponseSample;
import com.hust.baseweb.applications.postsys.model.posttrip.CreatePostTripModel;
import com.hust.baseweb.applications.postsys.model.posttrip.ExecuteTripInputModel;
import com.hust.baseweb.applications.postsys.model.posttrip.ExecuteTripOutputModel;
import com.hust.baseweb.applications.postsys.repo.PostTripExecuteRepo;
import com.hust.baseweb.applications.postsys.repo.PostFixedTripRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PostTripService {
    @Autowired
    PostFixedTripRepo postFixedTripRepo;

    @Autowired
    PostTripExecuteRepo postTripExecuteRepo;

    public List<PostFixedTrip> findAllVehicle() {
        return postFixedTripRepo.findAll();
    }

    public PostFixedTrip findByPostOfficeFixedTripId(String postOfficeFixedTripId) {
        return postFixedTripRepo.findByPostOfficeFixedTripId(UUID.fromString(postOfficeFixedTripId));
    }

    public PostFixedTrip createPostTrip(CreatePostTripModel input) {
        PostFixedTrip postFixedTrip = new PostFixedTrip();
        postFixedTrip.setFromPostOffice(new PostOffice());
        postFixedTrip.setToPostOffice(new PostOffice());
        postFixedTrip.getFromPostOffice().setPostOfficeId(input.getFromPostOfficeId());
        postFixedTrip.getToPostOffice().setPostOfficeId(input.getToPostOfficeId());
        postFixedTrip.setFromDate(input.getFromDate());
        postFixedTrip.setThruDate(input.getThruDate());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        postFixedTrip.setScheduleDepartureTime(simpleDateFormat.format(input.getSheduleDepatureTime()));
        if (postFixedTrip.getFromDate() == null) {
            postFixedTrip.setFromDate(new Date());
        }
        return postFixedTripRepo.save(postFixedTrip);
    }

    public List<PostFixedTrip> createVehicleList(List<CreatePostTripModel> createPostTripModels) {
        List<PostFixedTrip> postFixedTrips = new ArrayList<>();
        for (CreatePostTripModel createPostTripModel : createPostTripModels) {
            PostFixedTrip postFixedTrip = new PostFixedTrip();
            postFixedTrip.getFromPostOffice().setPostOfficeId(createPostTripModel.getFromPostOfficeId());
            postFixedTrip.getToPostOffice().setPostOfficeId(createPostTripModel.getToPostOfficeId());
            postFixedTrip.setFromDate(createPostTripModel.getFromDate());
            postFixedTrip.setThruDate(createPostTripModel.getThruDate());
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
        PostTripExecute postTripExecute = new PostTripExecute();
        postTripExecute.setDepartureDateTime(executeTripInputModel.getDepatureDateTime());
        postTripExecute.setPostOfficeFixedTripId(executeTripInputModel.getPostOfficeFixedTripId());
        postTripExecuteRepo.save(postTripExecute);
        return new ResponseSample("SUCCESS", "Execute Trip Success");
    }

    public ResponseSample updatePostTripExecute(ExecuteTripInputModel executeTripInputModel) {
        PostTripExecute postTripExecute = postTripExecuteRepo.findByPostOfficeFixedTripExecuteId(UUID.fromString(
            executeTripInputModel.getPostOfficeFixedTripExecuteId()));
        if (executeTripInputModel.getPostmanId() != null || executeTripInputModel.getPostmanId().length() > 0) {
            postTripExecute.setPostmanId(executeTripInputModel.getPostmanId());
        }
        if (executeTripInputModel.getStatus() != null || executeTripInputModel.getStatus().length() > 0) {
            postTripExecute.setStatus(executeTripInputModel.getStatus());
        }
        postTripExecuteRepo.save(postTripExecute);
        return new ResponseSample("SUCCESS", "Update Trip Success");
    }

    public List<ExecuteTripOutputModel> getExecuteTripByDate(Date date) {
        List<ExecuteTripOutputModel> executeTripOutputModels = new ArrayList<>();
        return executeTripOutputModels;
    }
}
