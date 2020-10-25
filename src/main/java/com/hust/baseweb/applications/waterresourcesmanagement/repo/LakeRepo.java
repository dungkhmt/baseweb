package com.hust.baseweb.applications.waterresourcesmanagement.repo;

import com.hust.baseweb.applications.waterresourcesmanagement.entity.Lake;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LakeRepo extends JpaRepository<Lake, String> {
    Lake findByLakeId(String lakeId);


}
