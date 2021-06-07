package com.hust.baseweb.applications.postsys.repo;

import com.hust.baseweb.applications.postsys.entity.PostShipOrderFixedTripPostOfficeAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostShipOrderFixedTripPostOfficeAssignmentRepo
    extends JpaRepository<PostShipOrderFixedTripPostOfficeAssignment, UUID> {

    List<PostShipOrderFixedTripPostOfficeAssignment> findByPostOfficeFixedTripExecuteId(UUID postOfficeFixedTripExecuteId);

    List<PostShipOrderFixedTripPostOfficeAssignment> findByPostOfficeFixedTripExecuteIdIn(List<UUID> postOfficeFixedTripExecuteIds);

    PostShipOrderFixedTripPostOfficeAssignment findByPostShipOrderTripPostOfficeAssignmentId(UUID postShipOrderTripPostOfficeAssignmentId);
}
