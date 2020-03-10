package com.hust.baseweb.applications.customer.service;

import java.util.List;

import org.springframework.stereotype.Service;


import com.hust.baseweb.applications.customer.entity.PartyDistributor;
import com.hust.baseweb.applications.customer.model.CreateDistributorInputModel;

@Service
public interface DistributorService {
	PartyDistributor save(CreateDistributorInputModel input);
	List<PartyDistributor> findDistributors();
}
