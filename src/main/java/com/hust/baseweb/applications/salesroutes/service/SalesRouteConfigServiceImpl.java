package com.hust.baseweb.applications.salesroutes.service;

import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hust.baseweb.applications.salesroutes.entity.SalesRouteConfig;
import com.hust.baseweb.applications.salesroutes.repo.PSalesRouteConfigRepo;

@Service
@Log4j2
public class SalesRouteConfigServiceImpl implements SalesRouteConfigService {
	@Autowired
	private PSalesRouteConfigRepo pSalesRouteConfigRepo;
	
	@Override
	public SalesRouteConfig save(String days, int repeatWeek) {
		// TODO Auto-generated method stub
		SalesRouteConfig salesRouteConfig = new SalesRouteConfig();
		salesRouteConfig.setDays(days);
		salesRouteConfig.setRepeatWeek(repeatWeek);
		
		salesRouteConfig = pSalesRouteConfigRepo.save(salesRouteConfig);
		
		return salesRouteConfig;
	}

}
