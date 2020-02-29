package com.hust.baseweb.applications.sales.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hust.baseweb.applications.sales.entity.PartySalesman;
import com.hust.baseweb.applications.sales.repo.PartySalesmanRepo;

@Service

public class PartySalesmanServiceImpl implements PartySalesmanService {
	@Autowired
	private PartySalesmanRepo partySalesmanRepo;
	
	@Override
	public List<PartySalesman> findAllSalesman() {
		// TODO Auto-generated method stub
		List<PartySalesman> salesman = partySalesmanRepo.findAll();
		return salesman;
	}

}
