package com.hust.baseweb.applications.salesroutes.entity;

import com.hust.baseweb.applications.sales.entity.RetailOutletSalesmanVendor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Setter
public class SalesRouteConfigRetailOutlet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "sales_route_config_retail_outlet_id")
    private UUID salesRouteConfigRetailOutletId;

    @JoinColumn(name = "sales_route_planning_period_id", referencedColumnName = "sales_route_planning_period_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private SalesRoutePlanningPeriod salesRoutePlanningPeriod;

    @JoinColumn(name = "visit_frequency_id", referencedColumnName = "visit_frequency_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private SalesRouteVisitFrequency salesRouteVisitFrequency;

    @JoinColumn(name = "sales_route_config_id", referencedColumnName = "sales_route_config_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private SalesRouteConfig salesRouteConfig;

    @JoinColumn(name = "retail_outlet_salesman_vendor_id", referencedColumnName = "retail_outlet_salesman_vendor_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private RetailOutletSalesmanVendor retailOutletSalesmanVendor;

    @Column(name = "start_execute_week")
    private Integer startExecuteWeek;

    @Column(name = "start_execute_date")
    private String startExecuteDate;

}
