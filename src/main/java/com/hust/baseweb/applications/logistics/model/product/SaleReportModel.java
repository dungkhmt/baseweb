package com.hust.baseweb.applications.logistics.model.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
public class SaleReportModel {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Input {
        private String fromDate;
        private String thruDate;
        private String productId;
        private String partyCustomerId;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class DateBasedInput {
        private String fromDate;
        private String thruDate;


    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Output {
        private List<DatePrice> datePrices;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class DatePrice {
        private String date;
        private Double price;

        public String getDate() {
            return date;
        }

        public Double getPrice() {
            return price;
        }
    }

}
