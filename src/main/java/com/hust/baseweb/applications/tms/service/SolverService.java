package com.hust.baseweb.applications.tms.service;

import com.hust.baseweb.applications.tms.model.DeliveryTripDetailModel;
import com.hust.baseweb.applications.tms.model.DeliveryTripModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.IOException;
import java.util.List;

public interface SolverService {

    boolean solve(SolverOption solverOption) throws IOException;

    TripSuggestion.Output suggestTrips(TripSuggestion.Input input);

    @AllArgsConstructor
    @Getter
    @Setter
    @NoArgsConstructor
    class SolverOption {

        private String deliveryPlanId;
        private Integer timeLimit; // second
    }

    class TripSuggestion {

        @AllArgsConstructor
        @Getter
        @Setter
        @NoArgsConstructor
        public static class Input {

            private String deliveryPlanId;
            private List<DeliveryTripDetailModel.Create> shipmentItems;
        }

        @AllArgsConstructor
        @Getter
        @Setter
        @NoArgsConstructor
        public static class Output {

            private List<Trip> trips;

            @AllArgsConstructor
            @Getter
            @Setter
            @NoArgsConstructor
            public static class Trip {

                private DeliveryTripModel deliveryTripModel;
                private Double extraTotalWeight;
                private Double extraTotalTime;
                private Double extraTotalDistance;
            }
        }
    }
}
