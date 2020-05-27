package com.hust.baseweb.applications.order.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetListSalesmanInputModel {

    private String statusId;

    public GetListSalesmanInputModel() {

        super();

    }

    public GetListSalesmanInputModel(String statusId) {

        super();
        this.statusId = statusId;
    }

}
