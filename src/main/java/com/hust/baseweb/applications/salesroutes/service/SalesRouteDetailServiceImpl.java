package com.hust.baseweb.applications.salesroutes.service;

import com.hust.baseweb.applications.customer.entity.PartyDistributor;
import com.hust.baseweb.applications.customer.entity.PartyRetailOutlet;
import com.hust.baseweb.applications.order.repo.PartyCustomerRepo;
import com.hust.baseweb.applications.sales.entity.PartySalesman;
import com.hust.baseweb.applications.sales.entity.RetailOutletSalesmanVendor;
import com.hust.baseweb.applications.sales.repo.PartySalesmanRepo;
import com.hust.baseweb.applications.salesroutes.entity.SalesRouteConfig;
import com.hust.baseweb.applications.salesroutes.entity.SalesRouteConfigRetailOutlet;
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

    private PSalesRouteConfigRetailOutletRepo pSalesRouteConfigCustomerRepo;
    private PSalesRoutePlanningPeriodRepo pSalesRoutePlanningPeriodRepo;
    private SalesRouteConfigRetailOutletRepo salesRouteConfigRetailOutletRepo;
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

        //List<SalesRouteConfigRetailOutlet> SRCC = salesRouteConfigRetailOutletRepo.findByPartySalesman(partySalesman);
        List<SalesRouteConfigRetailOutlet> SRCC = salesRouteConfigRetailOutletRepo.findBySalesRoutePlanningPeriod(SRPP);

        log.info("generateSalesRouteDetailOfSalesman, period = " + SRPP.getFromDate().toString() + ", toDate = " + SRPP.getToDate() + ", SRCC.sz = " + SRCC.size());
        Date startDate = SRPP.getFromDate();
        Date endDate = SRPP.getToDate();
        int cnt = 0;
        for (SalesRouteConfigRetailOutlet srcc : SRCC) {
            RetailOutletSalesmanVendor rosv = srcc.getRetailOutletSalesmanVendor();
            PartyRetailOutlet pc = rosv.getPartyRetailOutlet();// srcc.getPartyRetailOutlet();
            PartySalesman sm = rosv.getPartySalesman();
            PartyDistributor partyDistributor = rosv.getPartyDistributor();

            if(!sm.getPartyId().equals(partySalesmanId)) continue;

            SalesRouteConfig src = srcc.getSalesRouteConfig();
            String startExecuteDate = srcc.getStartExecuteDate();
            String[] days = src.getDays().split(",");
            for (String day : days) {
                int d = Integer.parseInt(day.trim());
                //List<String> dates = DateTimeUtils.getListDateHavingDay(d, DateTimeUtils.date2YYYYMMDD(startDate), DateTimeUtils.date2YYYYMMDD(endDate), startExecuteDate);
                List<String> dates = DateTimeUtils.getListDateHavingDay(d, startDate, endDate, startExecuteDate + " 00:00:00");
                for (String date : dates) {
                    log.info("generateSalesRouteDetailOfSalesman, get date " + date + " day " + d + " customer " + pc.getRetailOutletName() + ", salesman " + partySalesman.getPartyId());
                    SalesRouteDetail srd = new SalesRouteDetail();
                    srd.setExecuteDate(date);
                    srd.setPartyRetailOutlet(pc);
                    srd.setPartySalesman(partySalesman);
                    srd.setPartyDistributor(partyDistributor);
                    srd.setSalesRoutePlanningPeriod(SRPP);
                    srd.setSalesRouteConfigRetailOutlet(srcc);
                    srd = pSalesRouteDetailRepo.save(srd);
                    cnt++;
                }
            }
        }
        return cnt;
    }

    @Override
    public List<PartyRetailOutlet> getRetailOutletsVisitedSalesmanDay(
            UUID partySalesmanId, String date) {
        String executeDate = date;//DateTimeUtils.date2YYYYMMDD(date);

        log.info("getCustomersVisitedSalesmanDay, partySalesmanId = " + partySalesmanId + ", date = " + executeDate);
        PartySalesman partySalesman = partySalesmanRepo.findByPartyId(partySalesmanId);

        List<SalesRouteDetail> lst = salesRouteDetailRepo.findByPartySalesmanAndExecuteDate(partySalesman, executeDate);
        List<PartyRetailOutlet> retList = new ArrayList<PartyRetailOutlet>();
        for (SalesRouteDetail srd : lst) {
            retList.add(srd.getPartyRetailOutlet());
        }
        return retList;
        //*/
        //List<PartyRetailOutlet> retList = new ArrayList<PartyRetailOutlet>();
        //return retList;
    }


}
