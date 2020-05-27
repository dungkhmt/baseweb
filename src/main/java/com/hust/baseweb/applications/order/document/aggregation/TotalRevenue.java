package com.hust.baseweb.applications.order.document.aggregation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
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
public class TotalRevenue {

    @Id
    private LocalDate id;
    private double revenue;

    public double increase(double value) {

        revenue += value;
        return revenue;
    }


}
