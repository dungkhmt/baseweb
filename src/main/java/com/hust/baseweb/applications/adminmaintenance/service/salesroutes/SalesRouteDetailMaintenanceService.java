package com.hust.baseweb.applications.adminmaintenance.service.salesroutes;

import com.hust.baseweb.applications.sales.entity.PartySalesman;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface SalesRouteDetailMaintenanceService {
    public long deleteByPartySalesmanId(UUID partySalesmanId);
}
