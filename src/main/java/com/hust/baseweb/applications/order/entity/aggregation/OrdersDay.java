package com.hust.baseweb.applications.order.entity.aggregation;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrdersDay {

    private String date;// KEY  format yyyy-MM-dd, e.g., 2020-03-20
    private POrder[] orders;

}
