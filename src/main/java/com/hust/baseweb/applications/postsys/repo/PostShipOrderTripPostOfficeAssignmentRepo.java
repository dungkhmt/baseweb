package com.hust.baseweb.applications.postsys.repo;

import com.hust.baseweb.applications.postsys.entity.PostShipOrderTripPostOfficeAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PostShipOrderTripPostOfficeAssignmentRepo extends JpaRepository<PostShipOrderTripPostOfficeAssignment, UUID> {

}
