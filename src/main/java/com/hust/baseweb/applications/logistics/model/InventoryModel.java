package com.hust.baseweb.applications.logistics.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
public class InventoryModel {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class OrderHeader {
        private String orderId;
        private String customerName;
        private String orderDate;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class OrderItem {
        private String productId;
        private String productName;
        private Integer quantity;
        private Integer exportedQuantity;
        private String orderId;
    }


}
