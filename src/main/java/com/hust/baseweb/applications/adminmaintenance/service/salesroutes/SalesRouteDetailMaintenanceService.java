package com.hust.baseweb.applications.adminmaintenance.service.salesroutes;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface SalesRouteDetailMaintenanceService {

    long deleteByPartySalesmanId(UUID partySalesmanId);
}
