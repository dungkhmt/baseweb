package com.hust.baseweb.applications.salesroutes.repo;

import com.hust.baseweb.applications.salesroutes.entity.SalesRouteConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SalesRouteConfigRepo extends JpaRepository<SalesRouteConfig, UUID> {
    List<SalesRouteConfig> findAll();
}
