package com.hust.baseweb.applications.salesroutes.service;

import com.hust.baseweb.applications.salesroutes.entity.SalesRoutePlanningPeriod;
import com.hust.baseweb.applications.salesroutes.repo.PSalesRoutePlanningPeriodRepo;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Log4j2
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class SalesRoutePlanningPeriodServiceImpl implements
        SalesRoutePlanningPeriodService {

    private PSalesRoutePlanningPeriodRepo salesRoutePlanningPeriodRepo;

    @Override
    public SalesRoutePlanningPeriod save(String fromDateYYYYMMDD, String toDateYYYYMMDD,
                                         String description) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date fromDate = null;
        Date toDate = null;
        try {
            fromDate = formatter.parse(fromDateYYYYMMDD);
            toDate = formatter.parse(toDateYYYYMMDD);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        SalesRoutePlanningPeriod salesRoutePlanningPeriod = new SalesRoutePlanningPeriod();
        salesRoutePlanningPeriod.setFromDate(fromDate);
        salesRoutePlanningPeriod.setToDate(toDate);
        salesRoutePlanningPeriod.setDescription(description);

        salesRoutePlanningPeriod = salesRoutePlanningPeriodRepo.save(salesRoutePlanningPeriod);
        return salesRoutePlanningPeriod;

    }

}
