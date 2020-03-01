package com.hust.baseweb.applications.sales.service;

import org.springframework.stereotype.Service;

import com.hust.baseweb.applications.sales.entity.PartySalesman;
import com.hust.baseweb.applications.sales.model.customersalesman.GetSalesmanOutputModel;
import com.hust.baseweb.entity.UserLogin;

import java.util.List;
import java.util.UUID;

@Service
public interface PartySalesmanService {
	List<GetSalesmanOutputModel> findAllSalesman();

	UserLogin findUserLoginOfSalesmanId(UUID partySalesmanId);
}
