package com.hust.baseweb.applications.postsys.repo;

import com.hust.baseweb.applications.postsys.entity.PostTrip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostTripRepo extends JpaRepository<PostTrip, UUID> {
    List<PostTrip> findAll();
    PostTrip findByPostOfficeFixedTripId(UUID postOfficeFixedTripId);
    PostTrip save(PostTrip postTrip);
}
