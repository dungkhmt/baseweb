package com.hust.baseweb.applications.order.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter

public class GetTotalRevenueItemOutputModel {
    private String date;
    private BigDecimal revenue;

    public GetTotalRevenueItemOutputModel() {
        super();

    }

    public GetTotalRevenueItemOutputModel(String date, BigDecimal revenue) {
        super();
        this.date = date;
        this.revenue = revenue;
    }

}
