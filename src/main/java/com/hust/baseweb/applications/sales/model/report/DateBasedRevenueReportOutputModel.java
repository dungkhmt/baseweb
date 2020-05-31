package com.hust.baseweb.applications.sales.model.report;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DateBasedRevenueReportOutputModel {

    private List<DateBasedRevenueReportElement> revenueElements;

    public DateBasedRevenueReportOutputModel(List<DateBasedRevenueReportElement> lst) {
        this.revenueElements = lst;
    }
}
