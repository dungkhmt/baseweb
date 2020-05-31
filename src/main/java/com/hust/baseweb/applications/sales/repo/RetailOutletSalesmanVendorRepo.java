package com.hust.baseweb.applications.sales.repo;

import com.hust.baseweb.applications.customer.entity.PartyDistributor;
import com.hust.baseweb.applications.customer.entity.PartyRetailOutlet;
import com.hust.baseweb.applications.sales.entity.PartySalesman;
import com.hust.baseweb.applications.sales.entity.RetailOutletSalesmanVendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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

    List<RetailOutletSalesmanVendor> findAllByPartySalesmanAndPartyDistributorAndThruDate(PartySalesman partySalesman,
                                                                                          PartyDistributor partyDistributor,
                                                                                          Date thruDate);
    @Query( value = "select \tcast(retail_outlet_salesman_vendor_id as varchar) retailOutletSalesmanVendorId,\n" +
                    "\t\tretail_outlet_name retailOutletName\n" +
                    "from \tretail_outlet_salesman_vendor rosv \n" +
                    "\t\tinner join party_retail_outlet pro on rosv.party_retail_outlet_id = pro.party_id \n" +
                    "where \trosv.party_salesman_id = ?1\n" +
                    "\t\tand rosv.party_vendor_id = ?2",
        nativeQuery = true)
    List<GetRetailOutletsOfSalesmanAndDistributor>
        getRetailOutletsOfSalesmanAndDistributor(UUID partySalesmanId, UUID partyDistributorId);

    interface GetRetailOutletsOfSalesmanAndDistributor {
        String getRetailOutletSalesmanVendorId();
        String getRetailOutletName();
    }

}
