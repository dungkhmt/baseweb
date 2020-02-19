package com.hust.baseweb.applications.logistics.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImportInventoryItemInputModel {
    private String productId;
    private String facilityId;
    private String lotId;
    private String uomId;
    private int quantityOnHandTotal;

    public ImportInventoryItemInputModel() {
        super();

    }

}
