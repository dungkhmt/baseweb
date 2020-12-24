package com.hust.baseweb.applications.postsys.repo;

import com.hust.baseweb.applications.postsys.entity.Postman;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostmanRepo extends JpaRepository<Postman, UUID> {
    List<Postman> findByPostOfficeId(String postOfficeId);

    List<Postman> findByPostmanIdIn(List<UUID> postmanIds);
}
