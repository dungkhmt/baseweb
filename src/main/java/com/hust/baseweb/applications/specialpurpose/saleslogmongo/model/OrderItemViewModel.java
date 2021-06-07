package com.hust.baseweb.applications.specialpurpose.saleslogmongo.model;

import com.hust.baseweb.applications.logistics.entity.Uom;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemViewModel {

    private String productId;
    private String productName;
    private Uom uom;
    private int quantity;
}
