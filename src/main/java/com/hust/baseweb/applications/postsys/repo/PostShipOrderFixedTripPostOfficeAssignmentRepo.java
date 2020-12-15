package com.hust.baseweb.applications.postsys.repo;

import com.hust.baseweb.applications.postsys.entity.PostShipOrderFixedTripPostOfficeAssignment;
import com.hust.baseweb.applications.postsys.model.posttrip.PostShipOrderFixedTripPostOfficeAssignmentOM;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostShipOrderFixedTripPostOfficeAssignmentRepo
    extends JpaRepository<PostShipOrderFixedTripPostOfficeAssignment, UUID> {

    @Query(
        "select " +
            "psoftpoa.postShipOrderId as postShipOrderId, " +
            "psotpoa.deliveryOrder as deliveryOrder " +
        "from PostShipOrderFixedTripPostOfficeAssignment psoftpoa inner join psoftpoa.postShipOrderTripPostOfficeAssignment psotpoa " +
        "where psoftpoa.postShipOrderTripPostOfficeAssignment.postShipOrderId = ?1 ")
    List<PostShipOrderFixedTripPostOfficeAssignmentOM> findByPostShipOrderId(UUID postShipOrderId);
}
