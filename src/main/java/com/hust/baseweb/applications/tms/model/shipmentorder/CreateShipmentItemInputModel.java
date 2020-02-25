package com.hust.baseweb.applications.tms.model.shipmentorder;

import com.poiji.annotation.ExcelCellName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateShipmentItemInputModel {
    @ExcelCellName("QTY")
    private int quantity;

    @ExcelCellName("SL_PAL")
    private double pallet;

    @ExcelCellName("ITEM_NO")
    private String productId;

    @ExcelCellName("CUSTOMER")
    private String customerCode;

    @ExcelCellName("SITE_NUM")
    private String locationCode;

    @ExcelCellName("ADDRESS")
    private String address;
}
