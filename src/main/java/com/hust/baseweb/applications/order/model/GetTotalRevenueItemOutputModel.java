package com.hust.baseweb.applications.order.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetTotalRevenueItemOutputModel {
    private String date;
    private Double revenue;


}
