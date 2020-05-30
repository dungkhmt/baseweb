package com.hust.baseweb.applications.order.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetListPartyCustomerInputModel {

    private String statusId;

    public GetListPartyCustomerInputModel() {
        super();

    }

    public GetListPartyCustomerInputModel(String statusId) {
        super();
        this.statusId = statusId;
    }

}
