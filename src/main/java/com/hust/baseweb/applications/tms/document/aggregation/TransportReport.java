package com.hust.baseweb.applications.tms.document.aggregation;

import com.hust.baseweb.applications.tms.model.TransportReportModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
@Getter
@Setter
@AllArgsConstructor
public abstract class TransportReport<ID> {

    public abstract ID getId();

    private Long cost;
    private Integer totalDistance; // meter
    private Integer numberTrips;
    private Integer totalWeight;    // gram

    public <E extends TransportReport<ID>> E appendTo(E other) {
        other.setCost(other.getCost() + cost);
        other.setTotalDistance(other.getTotalDistance() + totalDistance);
        other.setNumberTrips(other.getNumberTrips() + numberTrips);
        other.setTotalWeight(other.getTotalWeight() + totalWeight);
        return other;
    }

    public TransportReportModel.DateReport toDateReport() {
        return new TransportReportModel.DateReport(
                null,
                cost,
                totalDistance,
                numberTrips,
                totalWeight
        );
    }
}
