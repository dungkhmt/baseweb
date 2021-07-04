package com.hust.baseweb.applications.waterresourcesmanagement.repo;

import com.hust.baseweb.applications.waterresourcesmanagement.entity.Lake;
import com.hust.baseweb.applications.waterresourcesmanagement.entity.LakeRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public interface LakeRoleRepo extends JpaRepository<LakeRole, UUID> {

    List<LakeRole> findAllByUserLoginIdAndThruDate(String userLoginId, Timestamp thruDate);

    List<LakeRole> findAllByUserLoginIdAndLakeAndThruDate(String userLoginId, Lake lake, Timestamp thruDate);
}
