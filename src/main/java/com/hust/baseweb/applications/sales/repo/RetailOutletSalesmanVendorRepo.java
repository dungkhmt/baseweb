package com.hust.baseweb.applications.sales.repo;

import com.hust.baseweb.applications.customer.entity.PartyDistributor;
import com.hust.baseweb.applications.customer.entity.PartyRetailOutlet;
import com.hust.baseweb.applications.sales.entity.PartySalesman;
import com.hust.baseweb.applications.sales.entity.RetailOutletSalesmanVendor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface RetailOutletSalesmanVendorRepo extends JpaRepository<RetailOutletSalesmanVendor, UUID> {

    RetailOutletSalesmanVendor findByRetailOutletSalesmanVendorId(UUID customerSalesmanVendorId);

    List<RetailOutletSalesmanVendor> findAllByPartySalesmanAndPartyRetailOutletAndPartyDistributorAndThruDate(
        PartySalesman partySalesman,
        PartyRetailOutlet partyRetailOutlet,
        PartyDistributor partyDistributor,
        Date thruDate
    );

    List<RetailOutletSalesmanVendor> findAllByPartyDistributorAndThruDate(
        PartyDistributor partyDistributor,
        Date thruDate
    );

    List<RetailOutletSalesmanVendor> findAllByPartyRetailOutletAndThruDate(
        PartyRetailOutlet partyRetailOutlet,
        Date thruDate
    );

    List<RetailOutletSalesmanVendor> findAllByPartySalesmanAndThruDate(PartySalesman partySalesman, Date thruDate);

    List<RetailOutletSalesmanVendor> findAllByPartySalesmanAndPartyDistributorAndThruDate(
        PartySalesman partySalesman,
        PartyDistributor partyDistributor,
        Date thruDate
    );
}
