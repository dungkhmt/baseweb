package com.hust.baseweb.applications.order.document.aggregation;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.UUID;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document
public class CustomerRevenue {

    @Id
    private CustomerRevenueId id;
    private double revenue;

    public double increase(double value) {
        revenue += value;
        return revenue;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @EqualsAndHashCode
    public static class CustomerRevenueId {
        private UUID customerId;
        private LocalDate date;
    }
}
