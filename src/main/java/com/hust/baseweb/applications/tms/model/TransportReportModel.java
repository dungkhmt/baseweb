package com.hust.baseweb.applications.tms.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
public class TransportReportModel {
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Input {
        private String fromDate;
        private String thruDate;
        private String facilityId;
        private String customerId;
        private String driverId;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Output {
        private List<DateReport> dateReports;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class DateReport {
        private String date;
        private Double cost;
        private Double totalDistance;
        private Double numberTrips;
        private Double totalWeight;
    }
}
