package com.hust.baseweb.applications.customer.model;

import com.hust.baseweb.applications.geo.model.PostalAddressModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PartyCustomerModel {
    private String partyCustomerId;
    private String partyTypeId;
    private String customerCode;
    private String customerName;
    //private List<PostalAddressModel> addresses;
}
