package com.hust.baseweb.repo;

import com.hust.baseweb.entity.Application;
import com.hust.baseweb.entity.ApplicationType;
import com.hust.baseweb.entity.SecurityPermission;
import org.springframework.data.repository.Repository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
@RepositoryRestResource(exported = false)
public interface ApplicationRepo  extends Repository<Application,String> {

    List<Application> findByTypeAndPermissionIn(ApplicationType type,List<SecurityPermission> permissions);
}
