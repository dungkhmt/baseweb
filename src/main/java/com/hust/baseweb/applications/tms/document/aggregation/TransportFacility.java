package com.hust.baseweb.applications.tms.document.aggregation;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
@Getter
@Setter
@Document
public class TransportFacility extends TransportReport<TransportFacility.Id> {

    @org.springframework.data.annotation.Id
    private Id id;

    public TransportFacility(Id id,
                             Long cost,
                             Integer totalDistance,
                             Integer numberTrips,
                             Integer totalWeight) {
        super(cost, totalDistance, numberTrips, totalWeight);
        this.id = id;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @EqualsAndHashCode
    public static class Id {
        private String facilityId;
        private LocalDate date;
    }
}
