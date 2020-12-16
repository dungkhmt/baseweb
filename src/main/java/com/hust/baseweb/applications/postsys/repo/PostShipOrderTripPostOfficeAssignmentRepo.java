package com.hust.baseweb.applications.postsys.repo;

import com.hust.baseweb.applications.postsys.entity.PostShipOrderTripPostOfficeAssignment;
import com.hust.baseweb.applications.postsys.model.posttrip.PostShipOrderTripPostOfficeAssignmentOM;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PostShipOrderTripPostOfficeAssignmentRepo
    extends JpaRepository<PostShipOrderTripPostOfficeAssignment, UUID> {

    @Query(
        "select max(psotpoa.deliveryOrder) as deliveryOrder, psotpoa.postShipOrderId as postShipOrderId " +
        "from PostShipOrderTripPostOfficeAssignment psotpoa where psotpoa.postShipOrderId = ?1 " +
        "group by psotpoa.postShipOrderId"
    )
    PostShipOrderTripPostOfficeAssignmentOM findByMaxDeliveryOrderPostShipOrderId(UUID postShipOrderId);
}
