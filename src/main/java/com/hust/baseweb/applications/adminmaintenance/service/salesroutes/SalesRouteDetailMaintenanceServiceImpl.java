package com.hust.baseweb.applications.adminmaintenance.service.salesroutes;

import com.hust.baseweb.applications.sales.repo.PartySalesmanRepo;
import com.hust.baseweb.applications.salesroutes.repo.SalesRouteDetailRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))

public class SalesRouteDetailMaintenanceServiceImpl implements SalesRouteDetailMaintenanceService {

    private SalesRouteDetailRepo salesRouteDetailRepo;
    private PartySalesmanRepo partySalesmanRepo;

    @Override
    public long deleteByPartySalesmanId(UUID partySalesmanId) {
        //PartySalesman partySalesman = partySalesmanRepo.findByPartyId(partySalesmanId);
        //salesRouteDetailRepo.deleteByPartySalesman(partySalesman);
        //salesRouteDetailRepo.deleteAll();
        // TODO: to be implemented
        return 0;
    }
}
