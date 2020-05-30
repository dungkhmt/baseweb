package com.hust.baseweb.applications.salesroutes.entity;

import com.hust.baseweb.applications.customer.entity.PartyDistributor;
import com.hust.baseweb.applications.customer.entity.PartyRetailOutlet;
import com.hust.baseweb.applications.sales.entity.PartySalesman;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Setter
public class SalesRouteDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID salesRouteDetailId;

    @JoinColumn(name = "party_salesman_id", referencedColumnName = "party_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private PartySalesman partySalesman;

    @JoinColumn(name = "party_retail_outlet_id", referencedColumnName = "party_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private PartyRetailOutlet partyRetailOutlet;

    @JoinColumn(name = "party_distributor_id", referencedColumnName = "party_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private PartyDistributor partyDistributor;

    @Column(name = "sequence")
    private int sequence;

    @Column(name = "execute_date")
    private String executeDate;// format YYYY-MM-DD

    @JoinColumn(name = "sales_route_config_retail_outlet_id",
                referencedColumnName = "sales_route_config_retail_outlet_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private SalesRouteConfigRetailOutlet salesRouteConfigRetailOutlet;

    @JoinColumn(name = "sales_route_planning_period_id", referencedColumnName = "sales_route_planning_period_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private SalesRoutePlanningPeriod salesRoutePlanningPeriod;

}
