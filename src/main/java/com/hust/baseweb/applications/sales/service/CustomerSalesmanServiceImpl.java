package com.hust.baseweb.applications.sales.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hust.baseweb.applications.customer.entity.PartyCustomer;
import com.hust.baseweb.applications.order.repo.PartyCustomerRepo;
import com.hust.baseweb.applications.sales.entity.CustomerSalesman;
import com.hust.baseweb.applications.sales.repo.CustomerSalesmanRepo;

@Service
@Log4j2
public class CustomerSalesmanServiceImpl implements CustomerSalesmanService {
	@Autowired
	private CustomerSalesmanRepo customerSalesmanRepo;

	@Autowired
	private PartyCustomerRepo partyCustomerRepo;

	@Override
	public List<PartyCustomer> getCustomersOfSalesman(UUID partySalesmanId) {
		// TODO: optimize this method, add condition filter where thru_date is
		// null
		List<CustomerSalesman> lst = customerSalesmanRepo.findAll();
		List<PartyCustomer> sel_list = new ArrayList<PartyCustomer>();
		List<PartyCustomer> allCustomers = partyCustomerRepo.findAll();
		for (CustomerSalesman cs : lst) {
			log.info("getCustomersOfSalesman, partySalesmanId = "
					+ partySalesmanId + ", cs.getPartySalesmanId = "
					+ cs.getPartySalesmanId());
			if (cs.getThruDate() == null
					&& cs.getPartySalesmanId().equals(partySalesmanId)) {
				// PartyCustomer c =
				// partyCustomerRepo.findByPartyId(cs.getPartyCustomerId());//
				// WHY thid does not work
				for (PartyCustomer c : allCustomers) {
					if (c.getPartyId().equals(cs.getPartyCustomerId())) {
						sel_list.add(c);
						log.info("getCustomersOfSalesman, partySalesmanId = "
								+ partySalesmanId
								+ ", cs.getPartySalesmanId = "
								+ cs.getPartySalesmanId() + ", add customer "
								+ c.getPartyId());
					}
				}
			}
		}
		return sel_list;
	}

	@Override
	public CustomerSalesman save(UUID partyCustomerId, UUID partySalesmanId) {
		CustomerSalesman cs = new CustomerSalesman();
		cs.setPartyCustomerId(partyCustomerId);
		cs.setPartySalesmanId(partySalesmanId);
		cs.setFromDate(new Date());
		customerSalesmanRepo.save(cs);
		return cs;
	}
}
