package com.hust.baseweb.applications.salesroutes.service;

import com.hust.baseweb.applications.salesroutes.entity.SalesRouteConfig;
import org.springframework.stereotype.Service;

@Service
public interface SalesRouteConfigService {
    public SalesRouteConfig save(String days, int repeatWeek);
}
