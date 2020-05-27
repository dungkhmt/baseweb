package com.hust.baseweb.applications.customer.model;

import com.hust.baseweb.applications.customer.entity.PartyDistributor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetListDistributorOutPutModel {

    List<PartyDistributor> lists;

    public GetListDistributorOutPutModel(List<PartyDistributor> list) {

        this.lists = list;
    }
}
