package com.hust.baseweb.applications.customer.service;

import com.hust.baseweb.applications.customer.entity.PartyContactMechPurpose;
import com.hust.baseweb.applications.customer.entity.PartyDistributor;
import com.hust.baseweb.applications.customer.entity.PartyRetailOutlet;
import com.hust.baseweb.applications.customer.model.CreateDistributorInputModel;
import com.hust.baseweb.applications.customer.model.DetailDistributorModel;
import com.hust.baseweb.applications.customer.repo.PartyContactMechPurposeRepo;
import com.hust.baseweb.applications.customer.repo.PartyRetailOutletRepo;
import com.hust.baseweb.applications.geo.entity.GeoPoint;
import com.hust.baseweb.applications.geo.entity.PostalAddress;
import com.hust.baseweb.applications.geo.repo.GeoPointRepo;
import com.hust.baseweb.applications.geo.repo.PostalAddressRepo;
import com.hust.baseweb.applications.order.repo.PartyDistributorRepo;
import com.hust.baseweb.applications.sales.entity.RetailOutletSalesmanVendor;
import com.hust.baseweb.applications.sales.model.retailoutletsalesmandistributor.RetailOutletSalesmanDistributorModel;
import com.hust.baseweb.applications.sales.repo.RetailOutletSalesmanVendorRepo;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

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
    private PartyDistributorRepo partyDistributorRepo;
    private RetailOutletSalesmanVendorRepo retailOutletSalesmanVendorRepo;
    private PartyRetailOutletRepo partyRetailOutletRepo;

    @Override
    @Transactional
    public PartyDistributor save(CreateDistributorInputModel input) {


        PartyType partyType = partyTypeRepo.findByPartyTypeId("PARTY_DISTRIBUTOR");

        //UUID partyId = UUID.randomUUID();
        //Party party = new Party();
        //party.setPartyId(partyId);// KHONG WORK vi partyId khi insert vao DB se duoc sinh tu dong, no se khac voi partyId sinh ra boi SPRING
        Party party = new Party(
            null,
            partyTypeRepo.getOne(PartyTypeEnum.PERSON.name()),
            "",
            statusRepo
                .findById(StatusEnum.PARTY_ENABLED.name())
                .orElseThrow(NoSuchElementException::new),
            false);
        party.setName(input.getDistributorName());
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
        distributor = partyDistributorRepo.save(distributor);
//        customerRepo.save(customer);


        GeoPoint geoPoint = null;
        if (input.getLatitude() != null && input.getLongitude() != null) {
            geoPoint = new GeoPoint();
            //UUID geoPointId = UUID.randomUUID();
            geoPoint.setLatitude(Double.parseDouble(input.getLatitude()));
            geoPoint.setLongitude(Double.parseDouble(input.getLongitude()));
            //geoPoint.setGeoPointId(geoPointId);// KHONG WORK vi khi save vao DB thi geoPointId se duoc sinh voi DB engine
            geoPoint = geoPointRepo.save(geoPoint);
            UUID geoPointId = geoPoint.getGeoPointId();
        }

        //UUID contactMechId = UUID.randomUUID();
        PostalAddress address = new PostalAddress();
        //address.setContactMechId(contactMechId);// KHONG WORL vi contactMechId se duoc sinh tu dong boi DB uuid_generate_v1()
        address.setAddress(input.getAddress());
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
        List<PartyDistributor> distributors = partyDistributorRepo.findByPartyType(partyType);
        log.info("findDistributors, got distributors.sz = " + distributors.size());

        return distributors;
    }

    @Override
    public PartyDistributor findByPartyId(UUID partyId) {

        return partyDistributorRepo.findByPartyId(partyId);
    }

    @Override
    public List<PartyDistributor> findAllByPartyIdIn(List<UUID> partyIds) {

        return partyDistributorRepo.findAllByPartyIdIn(partyIds);
    }

    @Override
    public Page<PartyDistributor> findAllByPartyIdIn(List<UUID> partyIds, Pageable page) {

        return partyDistributorRepo.findAllByPartyIdIn(partyIds, page);
    }

    @Override
    public DetailDistributorModel getDistributorDetail(UUID partyDistributorId) {

        PartyDistributor partyDistributor = partyDistributorRepo.findByPartyId(partyDistributorId);
        List<RetailOutletSalesmanVendor> retailOutletSalesmanVendors = retailOutletSalesmanVendorRepo.findAllByPartyDistributorAndThruDate(
            partyDistributor,
            null);

        DetailDistributorModel detailDistributorModel = new DetailDistributorModel();
        detailDistributorModel.setPartyDistributorId(partyDistributor.getPartyId());
        detailDistributorModel.setDistributorCode(partyDistributor.getDistributorCode());
        detailDistributorModel.setDistributorName(partyDistributor.getDistributorName());
        List<RetailOutletSalesmanDistributorModel> retailOutletSalesmanDistributorModels =
            retailOutletSalesmanVendors
                .stream()
                .map(o -> new RetailOutletSalesmanDistributorModel(o))
                .collect(Collectors.toList());

        detailDistributorModel.setRetailOutletSalesmanDistributorModels(retailOutletSalesmanDistributorModels);

        return detailDistributorModel;
    }

    @Override
    public List<PartyDistributor> getDistributorCandidates(UUID partyRetailOutletId) {
        /**
         * Return all distributor have not connected with retail outlet yet
         */

        PartyRetailOutlet partyRetailOutlet = partyRetailOutletRepo.findByPartyId(partyRetailOutletId);
        List<RetailOutletSalesmanVendor> retailOutletSalesmanVendors = retailOutletSalesmanVendorRepo.findAllByPartyRetailOutletAndThruDate(
            partyRetailOutlet,
            null);
        List<UUID> distributors = new ArrayList<>();
        List<PartyDistributor> distributorList;

        for (RetailOutletSalesmanVendor rosv : retailOutletSalesmanVendors) {
            distributors.add(rosv.getPartyDistributor().getPartyId());
        }

        if (0 == distributors.size()) {
            distributorList = partyDistributorRepo.findAll();
        } else {
            distributorList = partyDistributorRepo.findAllByPartyIdNotIn(distributors);
        }

        return distributorList;
    }

}
