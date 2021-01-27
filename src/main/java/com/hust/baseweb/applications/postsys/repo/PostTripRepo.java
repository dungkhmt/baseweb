package com.hust.baseweb.applications.postsys.repo;

import com.hust.baseweb.applications.postsys.entity.PostOfficeTrip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostTripRepo extends JpaRepository<PostOfficeTrip, UUID> {
    List<PostOfficeTrip> findAll();
    PostOfficeTrip findByPostOfficeTripId(UUID postOfficeTripId);
    PostOfficeTrip save(PostOfficeTrip postOfficetrip);

}
