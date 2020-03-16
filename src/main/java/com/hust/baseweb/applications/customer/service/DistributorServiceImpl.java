package com.hust.baseweb.applications.customer.service;

import com.hust.baseweb.applications.customer.entity.PartyContactMechPurpose;
import com.hust.baseweb.applications.customer.entity.PartyDistributor;
import com.hust.baseweb.applications.customer.model.CreateDistributorInputModel;
import com.hust.baseweb.applications.customer.repo.DistributorRepo;
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
public class DistributorServiceImpl implements DistributorService {
    private GeoPointRepo geoPointRepo;
    private PostalAddressRepo postalAddressRepo;
    private PartyRepo partyRepo;
    private PartyTypeRepo partyTypeRepo;
    private StatusRepo statusRepo;
    private PartyContactMechPurposeRepo partyContactMechPurposeRepo;
    private DistributorRepo distributorRepo;

    @Override
    @Transactional
    public PartyDistributor save(CreateDistributorInputModel input) {


        PartyType partyType = partyTypeRepo.findByPartyTypeId("PARTY_DISTRIBUTOR");

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

        PartyDistributor distributor = new PartyDistributor();
        distributor.setPartyId(partyId);
        distributor.setDistributorCode(input.getDistributorCode());
        //customer.setParty(party);
        distributor.setPartyType(partyType);
        distributor.setDistributorName(input.getDistributorName());
        distributor.setPostalAddress(new ArrayList<>());

        log.info("save, prepare save distributor partyId = " + distributor.getPartyId());
        distributor = distributorRepo.save(distributor);
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


        return distributor;
    }


    @Override
    public List<PartyDistributor> findDistributors() {
        PartyType partyType = partyTypeRepo.findByPartyTypeId("PARTY_DISTRIBUTOR");
        List<PartyDistributor> distributors = distributorRepo.findByPartyType(partyType);
        return distributors;
    }

}
