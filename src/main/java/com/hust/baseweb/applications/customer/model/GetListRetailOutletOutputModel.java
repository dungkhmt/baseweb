package com.hust.baseweb.applications.customer.model;

import com.hust.baseweb.applications.customer.entity.PartyRetailOutlet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetListRetailOutletOutputModel {
    private List<PartyRetailOutlet> list;
}
