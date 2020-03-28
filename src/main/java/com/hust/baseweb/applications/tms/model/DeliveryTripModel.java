package com.hust.baseweb.applications.tms.model;

import com.hust.baseweb.applications.geo.entity.GeoPoint;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DeliveryTripModel {
    private String deliveryPlanId;
    private String deliveryPlanSolutionSeqId;
    private String deliveryTripId;
    private String executeDate;
    private Double totalDistance;
    private Double totalWeight;
    private Double totalPallet;
    private Double totalExecutionTime;
    private Integer totalLocation;
    private String vehicleId;
    private String vehicleTypeId;
    private Double maxVehicleCapacity;
    private String userLoginId;
    private Double distance;

    @Getter
    @Setter
    @ToString
    public static class Create {
        private UUID deliveryPlanId;
        private String executeDate;
        private String vehicleId;
        private String driverId;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class LocationView {
        private String customerName;
        private String address;
        private double latitude;
        private double longitude;
        private UUID partyCustomerId;
        private List<LocationItemView> items;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class HeaderView {
        private UUID deliveryTripId;
        private String vehicleId;
        private UUID driverPartyId;
        private String driverUserLoginId;
        private Date executeDate;
        private List<LocationView> deliveryTripLocations;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Tour {
        private String deliveryTripId;
        private Double totalDistance;
        private Double totalWeight;
        private Double totalPallet;

        private List<GeoPoint> tour;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class LocationItemView {
        private UUID deliveryTripDetailId;
        private UUID shipmentItemId;
        private String productId;
        private String productName;
        private int deliveryQuantity;

    }
}
