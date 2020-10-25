package com.hust.baseweb.applications.postsys.repo;

import com.hust.baseweb.applications.postsys.entity.PostPackageType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PostPackageTypeRepo  extends JpaRepository<PostPackageType, String> {
    List<PostPackageType> findAll();
}
