package com.hust.baseweb.applications.order.document.aggregation;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document
public class ProductRevenue {

    @org.springframework.data.annotation.Id
    private ProductRevenue.Id id;
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
    public static class Id {

        private String productId;
        private LocalDate date;
    }
}
