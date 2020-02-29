package com.hust.baseweb.applications.sales.service;

import org.springframework.stereotype.Service;

import com.hust.baseweb.applications.sales.entity.PartySalesman;

import java.util.List;

@Service
public interface PartySalesmanService {
	List<PartySalesman> findAllSalesman();
}
