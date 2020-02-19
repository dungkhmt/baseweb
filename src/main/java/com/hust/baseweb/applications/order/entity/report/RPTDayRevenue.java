package com.hust.baseweb.applications.order.entity.report;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class RPTDayRevenue {
    private String date;
    private BigDecimal revenue;
    private int numberOrders;

    public RPTDayRevenue(String date, BigDecimal revenue, int numberOrders) {
        super();
        this.date = date;
        this.revenue = revenue;
        this.numberOrders = numberOrders;
    }

    public RPTDayRevenue() {
        super();

    }

}
