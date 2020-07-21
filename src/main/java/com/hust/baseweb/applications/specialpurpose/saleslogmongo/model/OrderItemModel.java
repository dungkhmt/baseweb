package com.hust.baseweb.applications.specialpurpose.saleslogmongo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderItemModel {

    private String productId;
    private Integer quantity;

}
