package com.hust.baseweb.applications.customer.model;

import com.hust.baseweb.applications.customer.entity.PartyCustomer;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetListCustomerOutputModel {
    List<PartyCustomer> lists;

    public GetListCustomerOutputModel(List<PartyCustomer> list) {
        super();
        this.lists = list;
    }
}
