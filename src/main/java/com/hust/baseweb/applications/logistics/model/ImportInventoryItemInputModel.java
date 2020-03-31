package com.hust.baseweb.applications.logistics.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ImportInventoryItemInputModel {
    private String productId;
    private String facilityId;
    private String lotId;
    private String uomId;
    private int quantityOnHandTotal;
}
