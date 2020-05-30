package com.hust.baseweb.applications.order.entity.aggregation;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RevenueAggregateUntilDate {

    private String date;// KEY, format yyyy-MM-dd
    private double totalRevenue;// total revenue aggregate until this date
    private List<ACustomerRevenue> customerRevenue;
    private List<AProductRevenue> productRevenue;
}
