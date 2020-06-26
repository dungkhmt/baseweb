package com.hust.baseweb.applications.sales.model.retailoutlet;

import com.hust.baseweb.applications.customer.entity.PartyRetailOutlet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ListRetailOutletOutputModel {

    private List<PartyRetailOutlet> partyRetailOutletList;
}
