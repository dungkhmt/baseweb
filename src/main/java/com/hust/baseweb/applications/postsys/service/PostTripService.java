package com.hust.baseweb.applications.postsys.service;

import com.hust.baseweb.applications.postsys.entity.PostOffice;
import com.hust.baseweb.applications.postsys.entity.PostTrip;
import com.hust.baseweb.applications.postsys.model.posttrip.CreatePostTripModel;
import com.hust.baseweb.applications.postsys.repo.PostTripRepo;
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
        postTrip.setFromDate(input.getFromDate());
        postTrip.setThruDate(input.getThruDate());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        postTrip.setScheduleDepartureTime(simpleDateFormat.format(input.getSheduleDepatureTime()));
        if (postTrip.getFromDate() == null) {
            postTrip.setFromDate(new Date());
        }
        return postTripRepo.save(postTrip);
    }

    public List<PostTrip> createVehicleList(List<CreatePostTripModel> createPostTripModels) {
        List<PostTrip> postTrips = new ArrayList<>();
        for (CreatePostTripModel createPostTripModel : createPostTripModels) {
            PostTrip postTrip = new PostTrip();
            postTrip.getFromPostOffice().setPostOfficeId(createPostTripModel.getFromPostOfficeId());
            postTrip.getToPostOffice().setPostOfficeId(createPostTripModel.getToPostOfficeId());
            postTrip.setFromDate(createPostTripModel.getFromDate());
            postTrip.setThruDate(createPostTripModel.getThruDate());
            if (postTrip.getFromDate() == null) {
                postTrip.setFromDate(new Date());
            }
            postTrips.add(postTrip);
        }
        return postTripRepo.saveAll(postTrips);
    }

    public void deleteByPostTripId(String postTripId) {
        PostTrip postTrip = postTripRepo.findByPostOfficeFixedTripId(UUID.fromString(postTripId));
        if (postTrip != null) {
            postTripRepo.delete(postTrip);
        }
    }
}
