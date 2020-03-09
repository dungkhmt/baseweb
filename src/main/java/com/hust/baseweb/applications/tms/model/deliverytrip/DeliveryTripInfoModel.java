package com.hust.baseweb.applications.tms.model.deliverytrip;

import com.hust.baseweb.applications.geo.entity.GeoPoint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DeliveryTripInfoModel {
    private String deliveryTripId;
    private Double totalDistance;
    private Double totalWeight;
    private Double totalPallet;

    private List<GeoPoint> tour;
}
