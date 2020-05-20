package com.hust.baseweb.applications.customer.service;


import com.hust.baseweb.applications.customer.entity.PartyContactMechPurpose;
import com.hust.baseweb.applications.customer.entity.PartyCustomer;
import com.hust.baseweb.applications.customer.model.CreateCustomerInputModel;
import com.hust.baseweb.applications.customer.repo.CustomerRepo;
import com.hust.baseweb.applications.customer.repo.PartyContactMechPurposeRepo;
import com.hust.baseweb.applications.geo.entity.GeoPoint;
import com.hust.baseweb.applications.geo.entity.PostalAddress;
import com.hust.baseweb.applications.geo.repo.GeoPointRepo;
import com.hust.baseweb.applications.geo.repo.PostalAddressRepo;
import com.hust.baseweb.entity.Party;
import com.hust.baseweb.entity.PartyType;
import com.hust.baseweb.entity.PartyType.PartyTypeEnum;
import com.hust.baseweb.entity.Status.StatusEnum;
import com.hust.baseweb.repo.PartyRepo;
import com.hust.baseweb.repo.PartyTypeRepo;
import com.hust.baseweb.repo.StatusRepo;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Log4j2
public class CustomerServiceImpl implements CustomerService {

    private CustomerRepo customerRepo;
    private GeoPointRepo geoPointRepo;
    private PostalAddressRepo postalAddressRepo;
    private PartyRepo partyRepo;
    private PartyTypeRepo partyTypeRepo;
    private StatusRepo statusRepo;
    private PartyContactMechPurposeRepo partyContactMechPurposeRepo;

    @Override
    @Transactional
    public PartyCustomer save(CreateCustomerInputModel input) {


        PartyType partyType = partyTypeRepo.findByPartyTypeId("PARTY_RETAIL_OUTLET");

        //UUID partyId = UUID.randomUUID();
        //Party party = new Party();
        //party.setPartyId(partyId);// KHONG WORK vi partyId khi insert vao DB se duoc sinh tu dong, no se khac voi partyId sinh ra boi SPRING
        Party party = new Party(null, partyTypeRepo.getOne(PartyTypeEnum.PERSON.name()), "",
            statusRepo.findById(StatusEnum.PARTY_ENABLED.name()).orElseThrow(NoSuchElementException::new),
            false);
        party.setType(partyType);

        partyRepo.save(party);

        UUID partyId = party.getPartyId();
        log.info("save party " + partyId);

        PartyCustomer customer = new PartyCustomer();
        customer.setPartyId(partyId);
        customer.setCustomerCode(input.getCustomerCode());
        //customer.setParty(party);
        customer.setPartyType(partyType);
        customer.setCustomerName(input.getCustomerName());
        customer.setPostalAddress(new ArrayList<>());

        log.info("save, prepare save customer partyId = " + customer.getPartyId());
        customer = customerRepo.save(customer);
//        customerRepo.save(customer);

        GeoPoint geoPoint = new GeoPoint();
        //UUID geoPointId = UUID.randomUUID();
        geoPoint.setLatitude(input.getLatitude());
        geoPoint.setLongitude(input.getLongitude());
        //geoPoint.setGeoPointId(geoPointId);// KHONG WORK vi khi save vao DB thi geoPointId se duoc sinh voi DB engine
        geoPoint = geoPointRepo.save(geoPoint);
        UUID geoPointId = geoPoint.getGeoPointId();

        //UUID contactMechId = UUID.randomUUID();
        PostalAddress address = new PostalAddress();
        //address.setContactMechId(contactMechId);// KHONG WORL vi contactMechId se duoc sinh tu dong boi DB uuid_generate_v1()
        address.setGeoPoint(geoPoint);

        address.setAddress(input.getAddress());


        address = postalAddressRepo.save(address);
        UUID contactMechId = address.getContactMechId();


        log.info("save, start save party_contact_mech_purpose");
        // write to PartyContactMech
        PartyContactMechPurpose partyContactMechPurpose = new PartyContactMechPurpose();
        partyContactMechPurpose.setContactMechId(contactMechId);
        partyContactMechPurpose.setPartyId(partyId);
        partyContactMechPurpose.setContactMechPurposeTypeId("PRIMARY_LOCATION");
        partyContactMechPurpose.setFromDate(new Date());
        partyContactMechPurposeRepo.save(partyContactMechPurpose);


        return customer;
    }


    @Override
    public List<PartyCustomer> findRetailOutlets() {
        PartyType partyType = partyTypeRepo.findByPartyTypeId("PARTY_RETAIL_OUTLET");
        List<PartyCustomer> retailOutlets = customerRepo.findByPartyType(partyType);
        return retailOutlets;
    }

    @Override
    public List<PartyCustomer> findAll() {
        return customerRepo.findAll();
    }

}
