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

    @Query(value = "select " +
                   "psotpoa1.post_ship_order_trip_post_office_assignment_id," +
                   "psotpoa1.post_office_trip_id," +
                   "psotpoa1.post_ship_order_id," +
                   "a.delivery_order," +
                   "psotpoa1.created_stamp " +
                   "from " +
                   "post_ship_order_trip_post_office_assignment psotpoa1 " +
                   "inner join (" +
                        "select max(psotpoa.delivery_order) delivery_order, psotpoa.post_ship_order_id " +
                        "from post_ship_order_trip_post_office_assignment psotpoa " +
                        "inner join post_ship_order pso on pso.post_ship_order_id = psotpoa.post_ship_order_id " +
                        "where psotpoa.created_stamp >= ?1 and psotpoa.created_stamp < ?2 " +
                        "and pso.status_id = ?3 " +
                        "group by psotpoa.post_ship_order_id " +
                   ") a on a.post_ship_order_id = psotpoa1.post_ship_order_id and a.delivery_order = psotpoa1.delivery_order", nativeQuery = true)
    List<PostShipOrderTripPostOfficeAssignment> findByMaxDeliveryOrderAndDate(Date fromDate, Date toDate, String statusId);

    List<PostShipOrderTripPostOfficeAssignment> findByPostOrder_PostShipOrderIdOrderByCreatedStampAsc(UUID postOrderId);
}
