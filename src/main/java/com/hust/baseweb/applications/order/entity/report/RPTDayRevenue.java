package com.hust.baseweb.applications.order.entity.report;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RPTDayRevenue {

    private String date;
    private Double revenue;
    private int numberOrders;

    public RPTDayRevenue(String date, Double revenue, int numberOrders) {
        super();
        this.date = date;
        this.revenue = revenue;
        this.numberOrders = numberOrders;
    }

    public RPTDayRevenue() {
        super();

    }

}
