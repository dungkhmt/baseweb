package com.hust.baseweb.applications.salesroutes.model.salesroutevisitfrequency;

import com.hust.baseweb.applications.salesroutes.entity.SalesRouteVisitFrequency;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ListSalesRouteVisitFrequencyOutputModel {

    private List<SalesRouteVisitFrequency> list;
}
