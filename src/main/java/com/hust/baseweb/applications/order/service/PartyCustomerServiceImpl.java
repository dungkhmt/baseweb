package com.hust.baseweb.applications.order.service;

import com.hust.baseweb.applications.customer.model.PartyCustomerModel;
import com.hust.baseweb.applications.order.repo.PartyCustomerRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PartyCustomerServiceImpl implements PartyCustomerService {

    private PartyCustomerRepo partyCustomerRepo;

    @Override
    public List<PartyCustomerModel> getListPartyCustomers() {

        return partyCustomerRepo.findAll().stream().map(partyCustomer -> new PartyCustomerModel(
            partyCustomer.getPartyId().toString(),
            partyCustomer.getPartyType().getPartyTypeId(),
            partyCustomer.getCustomerCode(),
            partyCustomer.getCustomerName()
            //partyCustomer.getPostalAddress().stream().map(a ->
            //        new PostalAddressModel(a.getAddress(),
            //                (a.getGeoPoint() != null ? a.getGeoPoint().getLatitude():0),
            //                (a.getGeoPoint() != null ? a.getGeoPoint().getLongitude():0)))
            //        .collect(Collectors.toList())
        )).collect(Collectors.toList());
    }

}
