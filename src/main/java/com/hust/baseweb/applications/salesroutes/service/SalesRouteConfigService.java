package com.hust.baseweb.applications.salesroutes.service;

import com.hust.baseweb.applications.salesroutes.entity.SalesRouteConfig;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SalesRouteConfigService {

    SalesRouteConfig save(String days, int repeatWeek);

    List<SalesRouteConfig> findAll();
}
