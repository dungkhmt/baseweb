package com.hust.baseweb.applications.tms.model.deliverytrip;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class DeliveryTripLocationView {
    private String customerName;
    private String address;
    private double latitude;
    private double longitude;
    private UUID partyCustomerId;
    private List<DeliveryTripLocationItemView> items;
}
