package com.hust.baseweb.applications.postsys.repo;

import com.hust.baseweb.applications.postsys.entity.PostShipOrderPostmanLastMileAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface PostShipOrderPostmanLastMileAssignmentRepo extends JpaRepository<PostShipOrderPostmanLastMileAssignment, UUID> {
    List<PostShipOrderPostmanLastMileAssignment> findAllByCreatedStampGreaterThanEqualAndCreatedStampLessThan(Date date1, Date date2);
    List<PostShipOrderPostmanLastMileAssignment> findByCreatedStampGreaterThanEqualAndCreatedStampLessThanAndPostmanId(Date date1, Date date2, UUID postmanId);
    List<PostShipOrderPostmanLastMileAssignment> findByStatusId(String statusId);

    List<PostShipOrderPostmanLastMileAssignment> findByPostShipOrderPostmanLastMileAssignmentIdIn(List<UUID> postShipOrderPostmanLastMileAssignmentIds);
}
