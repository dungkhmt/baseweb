package com.hust.baseweb.applications.salesroutes.service;

import com.hust.baseweb.applications.salesroutes.entity.SalesRouteConfig;
import com.hust.baseweb.applications.salesroutes.model.salesrouteconfig.GetListSalesRouteConfigOM;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SalesRouteConfigService {

    SalesRouteConfig save(String days, int repeatWeek);

    void createSalesRouteConfig(String visitFrequencyId, String days);

    List<SalesRouteConfig> findAll();

    List<GetListSalesRouteConfigOM> getListSalesRouteConfig();
}
