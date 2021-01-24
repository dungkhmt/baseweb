package com.hust.baseweb.applications.postsys.repo;

import com.hust.baseweb.applications.postsys.entity.PostDriver;
import com.hust.baseweb.applications.postsys.entity.Postman;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostDriverRepo extends JpaRepository<PostDriver, UUID> {
    List<PostDriver> findByPostDriverIdIn(List<UUID> postmanDriverIds);
    PostDriver findByPostDriverId(UUID postDriverId);
}
