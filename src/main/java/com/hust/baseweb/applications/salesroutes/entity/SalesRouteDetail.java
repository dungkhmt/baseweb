package com.hust.baseweb.applications.salesroutes.entity;

import com.hust.baseweb.applications.customer.entity.PartyCustomer;
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
    @ManyToOne(fetch = FetchType.EAGER)
    private PartySalesman partySalesman;

    @JoinColumn(name = "party_customer_id", referencedColumnName = "party_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private PartyCustomer partyCustomer;

    @Column(name = "sequence")
    private int sequence;

    @Column(name = "execute_date")
    private String executeDate;// format YYYY-MM-DD

    @JoinColumn(name = "sales_route_config_customer_id", referencedColumnName = "sales_route_config_customer_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private SalesRouteConfigCustomer salesRouteConfigCustomer;

    @JoinColumn(name = "sales_route_planning_period_id", referencedColumnName = "sales_route_planning_period_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private SalesRoutePlanningPeriod salesRoutePlanningPeriod;

}
