package com.hust.baseweb.applications.order.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetTotalRevenueInputModel {

    private String fromDate;
    private String toDate;

    public GetTotalRevenueInputModel(String fromDate, String toDate) {

        super();
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public GetTotalRevenueInputModel() {

        super();

    }

}
