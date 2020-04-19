package com.hust.baseweb.applications.tms.model;

import com.poiji.annotation.ExcelCellName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ShipmentItemModel {
    private String shipmentItemId;
    private Integer quantity;
    private Double pallet;
    private String productId;
    private String customerCode;
    private String locationCode;
    private String address;
    private String lat;
    private String lng;
    private Double weight;

    private Integer scheduledQuantity;
    private Integer completedQuantity;

    private String facilityId;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateDeliveryPlan {
        private String deliveryPlanId;
        private List<String> shipmentItemIds;
    }


    @Getter
    @Setter
    public static class Create {
        @ExcelCellName("QTY")
        private int quantity;

        @ExcelCellName("SL_PAL")
        private double pallet;

        @ExcelCellName("ITEM_NO")
        private String productId;

        @ExcelCellName("LANH_KHO")
        private String productTransportCategory;

        @ExcelCellName("ITEM_NAME")
        private String productName;

        @ExcelCellName("GRSS_WEIGHT")
        private Double weight;

        @ExcelCellName("UOM")
        private String uom;

        @ExcelCellName("CUSTOMER")
        private String customerCode;

        @ExcelCellName("CUSTOMER_NAME")
        private String customerName;

        @ExcelCellName("SITE_NUM")
        private String locationCode;

        @ExcelCellName("ADDRESS")
        private String address;

        @ExcelCellName("Ngày Book")
        private String orderDate;

        @ExcelCellName("LatLng")
        private String latLng;

        @ExcelCellName("SO_NUM")
        private String orderId;

        @ExcelCellName("HS_THU")
        private Integer hsThu;

        @ExcelCellName("HS_PAL")
        private Integer hsPal;

        @ExcelCellName("KHO_XUAT")
        private String facilityId;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DeleteDeliveryPlan {
        private String deliveryPlanId;
        private String shipmentItemId;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DeliveryPlan {
        private UUID shipmentItemId;
        private String productName;
        private Double weight;
        private Integer quantity;
        private Double pallet;
        private String address;
        private String latLng;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Info {
        private String shipmentItemId;
        private String orderId;
        private String facilityId;
        private String productId;
        private String productName;
        private Integer quantity;
        private String statusId;

        private List<DeliveryTripDetail> details;

        @Getter
        @Setter
        @AllArgsConstructor
        @NoArgsConstructor
        public static class DeliveryTripDetail {
            private String deliveryPlanId;
            private String deliveryTripId;
            private String executeDate;
            private Integer deliveryQuantity;
            private String vehicleId;
            private Double vehicleCapacity;
            private String driverId;
        }
    }

}
