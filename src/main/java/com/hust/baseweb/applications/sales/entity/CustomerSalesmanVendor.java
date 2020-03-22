package com.hust.baseweb.applications.sales.entity;

import com.hust.baseweb.applications.customer.entity.PartyCustomer;
import com.hust.baseweb.applications.customer.entity.PartyDistributor;
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
public class CustomerSalesmanVendor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "customer_salesman_vendor_id")
    private UUID customerSalesmanVendorId;

    @JoinColumn(name = "party_customer_id", referencedColumnName = "party_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private PartyCustomer partyCustomer;

    @JoinColumn(name = "party_salesman_id", referencedColumnName = "party_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private PartySalesman partySalesman;





    @JoinColumn(name = "party_vendor_id", referencedColumnName = "party_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private PartyDistributor partyDistributor;









    @Column(name = "from_date")
    private Date fromDate;

    @Column(name = "thru_date")
    private Date thruDate;


    @Transient
    private String customerCode;

    @Transient
    private String customerName;

    @Transient
    private String address;

    @Transient
    private String partyDistritorName;






}
