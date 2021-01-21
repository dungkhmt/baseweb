package com.hust.baseweb.applications.postsys.repo;

import com.hust.baseweb.applications.postsys.entity.PostFixedTrip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostFixedTripRepo extends JpaRepository<PostFixedTrip, UUID> {
    List<PostFixedTrip> findAll();
    PostFixedTrip findByPostOfficeFixedTripId(UUID postOfficeFixedTripId);
    PostFixedTrip findByPostOfficeTripId(UUID postOfficeTripId);
    PostFixedTrip save(PostFixedTrip postFixedTrip);
    List<PostFixedTrip> findByPostOfficeTrip_fromPostOfficeId(String fromPostOfficeId);
    List<PostFixedTrip> findByPostOfficeTrip_fromPostOfficeIdIn(List<String> fromPostOfficeIds);
    List<PostFixedTrip> findByPostOfficeFixedTripIdIn(List<UUID> postOfficeFixedTripIds);
}
