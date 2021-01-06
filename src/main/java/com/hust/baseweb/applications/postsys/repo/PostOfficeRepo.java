package com.hust.baseweb.applications.postsys.repo;

import com.hust.baseweb.applications.postsys.entity.PostOffice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PostOfficeRepo extends JpaRepository<PostOffice, String> {

    PostOffice save(PostOffice postOffice);

    PostOffice findByPostOfficeId(String postOfficeId);

    List<PostOffice> findByPostOfficeIdIn(List<String> postOfficeId);

    List<PostOffice> findAll();

    void deleteById(String postOfficeId);
}
