package com.hust.baseweb.applications.customer.service;

import com.hust.baseweb.applications.customer.entity.PartyDistributor;
import com.hust.baseweb.applications.customer.model.CreateDistributorInputModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DistributorService {
    PartyDistributor save(CreateDistributorInputModel input);

    List<PartyDistributor> findDistributors();
}
