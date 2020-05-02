package com.hust.baseweb.applications.salesroutes.model.salesrouteplanningperiod;

import com.hust.baseweb.applications.salesroutes.entity.SalesRoutePlanningPeriod;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetSalesRoutePlanningPeriodOutputModel {
    private List<SalesRoutePlanningPeriod> salesRoutePlanningPeriodList;
}
