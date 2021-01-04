package com.hust.baseweb.applications.postsys.repo;

import com.hust.baseweb.applications.postsys.entity.PostTripExecute;
import com.hust.baseweb.applications.postsys.model.posttrip.ExecuteTripOutputModelOM;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface PostTripExecuteRepo extends JpaRepository<PostTripExecute, UUID> {
    List<PostTripExecute> findAll();
    PostTripExecute findByPostOfficeFixedTripExecuteId(UUID postOfficeFixedTripExecuteId);
    PostTripExecute save(PostTripExecute postTripExecute);
    List<PostTripExecute> findAllByCreatedStampGreaterThanEqualAndCreatedStampLessThan(Date fromDate, Date toDate);
    @Query(value = "select " + 
           "cast(pofte.post_office_fixed_trip_execute_id as varchar) postOfficeFixedTripExecuteId," +
           "cast(pofte.post_office_fixed_trip_id as varchar) postOfficeFixedTripId," +
           "pofte.status status " +
           "from post_office_fixed_trip_execute pofte " +
           "inner join post_office_fixed_trip poft on pofte.post_office_fixed_trip_id = poft.post_office_fixed_trip_id " +
           "inner join post_office_trip pot on poft.post_office_trip_id = pot.post_office_trip_id " +
           "where pofte.created_stamp>=? and pofte.created_stamp<? and " +
           "pot.from_post_office_id = ?", nativeQuery = true)
    List<ExecuteTripOutputModelOM> findAllByDateAndFromPostOfficeId(Date fromDate, Date toDate, String postOfficeId);
}
