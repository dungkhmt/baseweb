package com.hust.baseweb.applications.sales.entity;

import com.hust.baseweb.applications.customer.entity.PartyDistributor;
import com.hust.baseweb.applications.customer.entity.PartyRetailOutlet;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
public class RetailOutletSalesmanVendor {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "retail_outlet_salesman_vendor_id")
    private UUID retailOutletSalesmanVendorId;

    @JoinColumn(name = "party_retail_outlet_id", referencedColumnName = "party_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private PartyRetailOutlet partyRetailOutlet;

    @JoinColumn(name = "party_salesman_id", referencedColumnName = "party_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private PartySalesman partySalesman;


    @JoinColumn(name = "party_vendor_id", referencedColumnName = "party_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private PartyDistributor partyDistributor;

    @Column(name = "from_date")
    private Date fromDate;

    @Column(name = "thru_date")
    private Date thruDate;


    @Transient
    private String retailOutletCode;

    @Transient
    private String retailOutletName;

    @Transient
    private String address;

    @Transient
    private String partyDistributorName;

}
