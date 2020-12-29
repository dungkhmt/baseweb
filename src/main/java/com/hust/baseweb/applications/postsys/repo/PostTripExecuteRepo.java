package com.hust.baseweb.applications.postsys.repo;

import com.hust.baseweb.applications.postsys.entity.PostTripExecute;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
