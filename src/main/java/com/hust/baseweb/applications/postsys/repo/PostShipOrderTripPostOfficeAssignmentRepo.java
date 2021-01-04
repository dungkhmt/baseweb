package com.hust.baseweb.applications.postsys.repo;

import com.hust.baseweb.applications.postsys.entity.PostShipOrderTripPostOfficeAssignment;
import com.hust.baseweb.applications.postsys.model.posttrip.PostShipOrderTripPostOfficeAssignmentOM;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
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

    @Query("select new PostShipOrderTripPostOfficeAssignment (" +
               "psopoa.postShipOrderPostOfficeTripAssignmentId, " +
               "psopoa.postShipOrderId," +
               "psopoa.postOrder," +
               "psopoa.postOfficeTripId," +
               "psopoa.postOfficeTrip," +
               "max(psopoa.deliveryOrder)," +
               "psopoa.createdStamp) " +
           "from PostShipOrderTripPostOfficeAssignment psopoa " +
           "where psopoa.createdStamp >= ?1 and psopoa.createdStamp < ?2 " +
           "and psopoa.postOrder.statusId = ?3 " +
           "group by psopoa.postOfficeTripId, psopoa.postShipOrderId")
    List<PostShipOrderTripPostOfficeAssignment> findByMaxDeliveryOrderAndDate(Date fromDate, Date toDate, String statusId);
}
