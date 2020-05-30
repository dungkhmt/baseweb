package com.hust.baseweb.applications.salesroutes.model.salesrouteconfig;

import com.hust.baseweb.applications.salesroutes.entity.SalesRouteConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetListSalesRouteConfigOutputModel {

    private List<SalesRouteConfig> salesRouteConfigList;
}
