package com.hust.baseweb.applications.postsys.service;

import com.hust.baseweb.applications.postsys.entity.PostOffice;
import com.hust.baseweb.applications.postsys.entity.PostTrip;
import com.hust.baseweb.applications.postsys.model.posttrip.CreatePostTripModel;
import com.hust.baseweb.applications.postsys.repo.PostTripRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PostTripService {
    @Autowired
    PostTripRepo postTripRepo;
    public List<PostTrip> findAllVehicle() {
        return postTripRepo.findAll();
    }

    public PostTrip findByPostOfficeFixedTripId(String postOfficeFixedTripId) {
        return postTripRepo.findByPostOfficeFixedTripId(UUID.fromString(postOfficeFixedTripId));
    }

    public PostTrip createPostTrip(CreatePostTripModel input) {
        PostTrip postTrip = new PostTrip();
        postTrip.setFromPostOffice(new PostOffice());
        postTrip.setToPostOffice(new PostOffice());
        postTrip.getFromPostOffice().setPostOfficeId(input.getFromPostOfficeId());
        postTrip.getToPostOffice().setPostOfficeId(input.getToPostOfficeId());
        postTrip.setFromDate((Date) input.getFromDate());
        postTrip.setThruDate((Date) input.getThruDate());
        return postTripRepo.save(postTrip);
    }

    public List<PostTrip> createVehicleList(List<CreatePostTripModel> createPostTripModels) {
        List<PostTrip> postTrips = new ArrayList<>();
        for (CreatePostTripModel createPostTripModel : createPostTripModels) {
            PostTrip postTrip = new PostTrip();
            postTrip.getFromPostOffice().setPostOfficeId(createPostTripModel.getFromPostOfficeId());
            postTrip.getToPostOffice().setPostOfficeId(createPostTripModel.getToPostOfficeId());
            postTrip.setFromDate((Date) createPostTripModel.getFromDate());
            postTrip.setThruDate((Date) createPostTripModel.getThruDate());
            postTrips.add(postTrip);
        }
        return postTripRepo.saveAll(postTrips);
    }
}
