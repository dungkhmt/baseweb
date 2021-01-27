package com.hust.baseweb.applications.postsys.repo;

import com.hust.baseweb.applications.postsys.entity.PostDriverPostOfficeAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostDriverPostOfficeAssignmentRepo extends JpaRepository<PostDriverPostOfficeAssignment, UUID> {
    List<PostDriverPostOfficeAssignment> findByPostDriverId(UUID postDriverId);
}
