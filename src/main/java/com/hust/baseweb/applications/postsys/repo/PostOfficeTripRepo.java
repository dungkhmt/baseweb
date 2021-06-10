package com.hust.baseweb.applications.postsys.repo;

import com.hust.baseweb.applications.postsys.entity.PostOfficeTrip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PostOfficeTripRepo extends JpaRepository<PostOfficeTrip, UUID> {

    PostOfficeTrip findByFromPostOfficeIdAndToPostOfficeId(String fromPostOfficeId, String toPostOfficeId);
}
