package com.hust.baseweb.applications.salesroutes.repo;

import com.hust.baseweb.applications.salesroutes.entity.SalesRouteConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface SalesRouteConfigRepo extends JpaRepository<SalesRouteConfig, UUID> {

    List<SalesRouteConfig> findAll();

    @Modifying
    @Transactional
    @Query(value = "insert into sales_route_config (visit_frequency_id , days , repeat_week) " +
                   "select ?1 , ?2 , ?3 where not exists (" +
                   "select * " +
                   "from sales_route_config src " +
                   "where visit_frequency_id = ?1 " +
                   "and days = ?2 )",
           nativeQuery = true)
    void createSalesRouteConfig(String visitFrequencyId, String days, int repeatWeek);
}
