package com.hust.baseweb.applications.salesroutes.service;

import org.springframework.stereotype.Service;

import com.hust.baseweb.applications.salesroutes.entity.SalesRouteConfig;

@Service
public interface SalesRouteConfigService {
	public SalesRouteConfig save(String days, int repeatWeek);
}
