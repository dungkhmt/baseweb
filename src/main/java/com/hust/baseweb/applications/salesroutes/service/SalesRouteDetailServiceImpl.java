package com.hust.baseweb.applications.salesroutes.service;

import com.hust.baseweb.applications.customer.entity.PartyCustomer;
import com.hust.baseweb.applications.order.repo.PartyCustomerRepo;
import com.hust.baseweb.applications.sales.entity.PartySalesman;
import com.hust.baseweb.applications.sales.repo.PartySalesmanRepo;
import com.hust.baseweb.applications.salesroutes.entity.SalesRouteConfig;
import com.hust.baseweb.applications.salesroutes.entity.SalesRouteConfigCustomer;
import com.hust.baseweb.applications.salesroutes.entity.SalesRouteDetail;
import com.hust.baseweb.applications.salesroutes.entity.SalesRoutePlanningPeriod;
import com.hust.baseweb.applications.salesroutes.repo.*;
import com.hust.baseweb.utils.DateTimeUtils;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Log4j2
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class SalesRouteDetailServiceImpl implements SalesRouteDetailService {

    private PSalesRouteConfigCustomerRepo pSalesRouteConfigCustomerRepo;
    private PSalesRoutePlanningPeriodRepo pSalesRoutePlanningPeriodRepo;
    private SalesRouteConfigCustomerRepo salesRouteConfigCustomerRepo;
    private PartySalesmanRepo partySalesmanRepo;
    private PartyCustomerRepo partyCustomerRepo;
    private PSalesRouteDetailRepo pSalesRouteDetailRepo;
    private SalesRouteDetailRepo salesRouteDetailRepo;

    @Override
    @Transactional
    public int generateSalesRouteDetailOfSalesman(UUID partySalesmanId, UUID salesRoutePlanningPeriodId) {
        log.info("generateSalesRouteDetailOfSalesman, salesmanId = " + partySalesmanId);
        PartySalesman partySalesman = partySalesmanRepo.findByPartyId(partySalesmanId);

        SalesRoutePlanningPeriod SRPP = pSalesRoutePlanningPeriodRepo.findBySalesRoutePlanningPeriodId(salesRoutePlanningPeriodId);

        List<SalesRouteConfigCustomer> SRCC = salesRouteConfigCustomerRepo.findByPartySalesman(partySalesman);

        log.info("generateSalesRouteDetailOfSalesman, period = " + SRPP.getFromDate().toString() + ", toDate = " + SRPP.getToDate() + ", SRCC.sz = " + SRCC.size());
        Date startDate = SRPP.getFromDate();
        Date endDate = SRPP.getToDate();
        int cnt = 0;
        for (SalesRouteConfigCustomer srcc : SRCC) {
            PartyCustomer pc = srcc.getPartyCustomer();
            SalesRouteConfig src = srcc.getSalesRouteConfig();
            String startExecuteDate = srcc.getStartExecuteDate();
            String[] days = src.getDays().split(",");
            for (String day : days) {
                int d = Integer.parseInt(day.trim());
                //List<String> dates = DateTimeUtils.getListDateHavingDay(d, DateTimeUtils.date2YYYYMMDD(startDate), DateTimeUtils.date2YYYYMMDD(endDate), startExecuteDate);
                List<String> dates = DateTimeUtils.getListDateHavingDay(d, startDate, endDate, startExecuteDate + " 00:00:00");
                for (String date : dates) {
                    log.info("generateSalesRouteDetailOfSalesman, get date " + date + " day " + d + " customer " + pc.getCustomerName() + ", salesman " + partySalesman.getPartyId());
                    SalesRouteDetail srd = new SalesRouteDetail();
                    srd.setExecuteDate(date);
                    srd.setPartyCustomer(pc);
                    srd.setPartySalesman(partySalesman);
                    srd.setSalesRoutePlanningPeriod(SRPP);
                    srd.setSalesRouteConfigCustomer(srcc);
                    srd = pSalesRouteDetailRepo.save(srd);
                    cnt++;
                }
            }
        }
        return cnt;
    }

    @Override
    public List<PartyCustomer> getCustomersVisitedSalesmanDay(
            UUID partySalesmanId, String date) {
        String executeDate = date;//DateTimeUtils.date2YYYYMMDD(date);

        log.info("getCustomersVisitedSalesmanDay, partySalesmanId = " + partySalesmanId + ", date = " + executeDate);
        PartySalesman partySalesman = partySalesmanRepo.findByPartyId(partySalesmanId);

        List<SalesRouteDetail> lst = salesRouteDetailRepo.findByPartySalesmanAndExecuteDate(partySalesman, executeDate);
        List<PartyCustomer> retList = new ArrayList<PartyCustomer>();
        for (SalesRouteDetail srd : lst) {
            retList.add(srd.getPartyCustomer());
        }
        return retList;
    }

}
