package com.hust.baseweb.applications.sales.service;

import com.hust.baseweb.applications.sales.entity.PartySalesman;
import com.hust.baseweb.applications.sales.model.customersalesman.GetSalesmanOutputModel;
import com.hust.baseweb.entity.Person;
import com.hust.baseweb.entity.UserLogin;
import com.hust.baseweb.model.PersonModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface PartySalesmanService {
    List<GetSalesmanOutputModel> findAllSalesman();

    UserLogin findUserLoginOfSalesmanId(UUID partySalesmanId);

    PartySalesman findById(UUID partyId);

    Person findPersonByPartyId(UUID partyId);

    PartySalesman save(PersonModel salesman);
}
