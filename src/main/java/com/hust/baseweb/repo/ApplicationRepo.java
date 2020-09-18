package com.hust.baseweb.repo;

import java.util.List;

import com.hust.baseweb.entity.Application;
import com.hust.baseweb.entity.ApplicationType;
import com.hust.baseweb.entity.SecurityPermission;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepo extends JpaRepository<Application, String> {

    List<Application> findByTypeAndPermissionIn(ApplicationType type, List<SecurityPermission> permissions);
}
