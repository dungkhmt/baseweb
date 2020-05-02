package com.hust.baseweb.applications.sales.model.distributor;

import com.hust.baseweb.applications.customer.entity.PartyDistributor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ListDistributorOutputModel {
    private List<PartyDistributor> partyDistributorList;
}
