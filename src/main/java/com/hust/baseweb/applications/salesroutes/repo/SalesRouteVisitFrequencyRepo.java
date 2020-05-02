package com.hust.baseweb.applications.salesroutes.repo;

import com.hust.baseweb.applications.salesroutes.entity.SalesRouteVisitFrequency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalesRouteVisitFrequencyRepo extends JpaRepository<SalesRouteVisitFrequency, String> {
    SalesRouteVisitFrequency findByVisitFrequencyId(String visitFrequencyId);

}
