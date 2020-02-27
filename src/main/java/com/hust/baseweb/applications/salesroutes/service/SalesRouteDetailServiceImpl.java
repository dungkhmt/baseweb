package com.hust.baseweb.applications.salesroutes.service;

import java.util.UUID;

import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hust.baseweb.applications.salesroutes.repo.PSalesRouteConfigCustomerRepo;
import com.hust.baseweb.applications.salesroutes.repo.PSalesRoutePlanningPeriodRepo;

@Service
@Log4j2
public class SalesRouteDetailServiceImpl implements SalesRouteDetailService {

	@Autowired
	private PSalesRouteConfigCustomerRepo salesRouteConfigCustomerRepo;
	
	@Autowired
	private PSalesRoutePlanningPeriodRepo salesRoutePlanningPeriodRepo;
	
	@Override
	public void generateSalesRouteDetailOfSalesman(UUID partySalesmanId) {
		// TODO Auto-generated method stub

		log.info("generateSalesRouteDetailOfSalesman, DO NOTHING->TODO");
	}

}
