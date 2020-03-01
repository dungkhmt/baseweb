package com.hust.baseweb.applications.sales.service;

import org.springframework.stereotype.Service;

import com.hust.baseweb.applications.sales.entity.PartySalesman;
import com.hust.baseweb.applications.sales.model.customersalesman.GetSalesmanOutputModel;

import java.util.List;

@Service
public interface PartySalesmanService {
	List<GetSalesmanOutputModel> findAllSalesman();
}
