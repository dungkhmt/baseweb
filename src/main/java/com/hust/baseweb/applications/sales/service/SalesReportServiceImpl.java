package com.hust.baseweb.applications.sales.service;

import com.hust.baseweb.applications.logistics.model.product.SaleReportModel;
import com.hust.baseweb.applications.order.entity.OrderHeader;
import com.hust.baseweb.applications.order.repo.OrderHeaderRepo;
import com.hust.baseweb.applications.order.repo.mongodb.CustomerRevenueRepo;
import com.hust.baseweb.applications.order.repo.mongodb.ProductRevenueRepo;
import com.hust.baseweb.applications.order.repo.mongodb.TotalRevenueRepo;
import com.hust.baseweb.applications.sales.model.report.DateBasedRevenueReportElement;
import com.hust.baseweb.applications.sales.model.report.DateBasedRevenueReportOutputModel;
import com.hust.baseweb.utils.Constant;
import com.hust.baseweb.utils.DateTimeUtils;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Log4j2
public class SalesReportServiceImpl implements SalesReportService {
    private OrderHeaderRepo orderHeaderRepo;
    private TotalRevenueRepo totalRevenueRepo;
    private CustomerRevenueRepo customerRevenueRepo;
    private ProductRevenueRepo productRevenueRepo;

    @Override
    public SaleReportModel.Output getSaleReports(SaleReportModel.Input input) {
        if (input.getProductId() != null) {
            List<SaleReportModel.DatePrice> datePrices = productRevenueRepo.findAllById_ProductIdAndId_DateBetween(
                    input.getProductId(),
                    LocalDate.parse(input.getFromDate(), Constant.LOCAL_DATE_TIME_FORMAT),
                    LocalDate.parse(input.getThruDate(), Constant.LOCAL_DATE_TIME_FORMAT).plusDays(1))
                    .stream()
                    .map(productRevenue -> new SaleReportModel.DatePrice(productRevenue.getId()
                            .getDate()
                            .format(Constant.LOCAL_DATE_FORMAT), productRevenue.getRevenue()))
                    .collect(Collectors.toList());
            return new SaleReportModel.Output(datePrices);

        } else if (input.getPartyCustomerId() != null) {
            List<SaleReportModel.DatePrice> datePrices = customerRevenueRepo.findAllById_CustomerIdAndId_DateBetween(
                    UUID.fromString(input.getPartyCustomerId()),
                    LocalDate.parse(input.getFromDate(), Constant.LOCAL_DATE_TIME_FORMAT),
                    LocalDate.parse(input.getThruDate(), Constant.LOCAL_DATE_TIME_FORMAT).plusDays(1))
                    .stream()
                    .map(customerRevenue -> new SaleReportModel.DatePrice(customerRevenue.getId()
                            .getDate()
                            .format(Constant.LOCAL_DATE_FORMAT), customerRevenue.getRevenue()))
                    .collect(Collectors.toList());
            return new SaleReportModel.Output(datePrices);
        }
        return null;
    }

    @Override
    public DateBasedRevenueReportOutputModel computeDateBasedRevenue(
            String fromDateStr, String toDateStr) {
        // TODO Auto-generated method stub
        try {
            Date fromDate = Constant.DATE_FORMAT.parse(fromDateStr + " 00:00:00");
            Date toDate = Constant.DATE_FORMAT.parse(toDateStr + " 23:59:59");
            HashMap<String, Double> mDate2Revenue = new HashMap<String, Double>();
            List<OrderHeader> orders = orderHeaderRepo.findAllByOrderDateBetween(fromDate, toDate);
            for (OrderHeader o : orders) {
                //log.info("computeDateBasedRevenue, order " + o.getOrderId() + ", date " + o.getOrderDate() + ", revenue = " + o.getGrandTotal());
                //String yyyymmhh = o.getOrderDate().getYear() + "-" + o.getOrderDate().getMonth() + "-" + o.getOrderDate().getDate();
                String yyyyMMdd = DateTimeUtils.date2YYYYMMDD(o.getOrderDate());
                log.info("computeDateBasedRevenue, on date " +
                        yyyyMMdd +
                        ": has order " +
                        o.getOrderId() +
                        ", date " +
                        o.getOrderDate() +
                        ", revenue = " +
                        o.getGrandTotal());
                if (mDate2Revenue.get(yyyyMMdd) == null) {
                    mDate2Revenue.put(yyyyMMdd, o.getGrandTotal());
                } else {
                    mDate2Revenue.put(yyyyMMdd, mDate2Revenue.get(yyyyMMdd) + o.getGrandTotal());
                }
            }
            List<DateBasedRevenueReportElement> dbrre = new ArrayList<DateBasedRevenueReportElement>();//[mDate2Revenue.keySet().size()];
            //int idx = -1;
            for (String k : mDate2Revenue.keySet()) {
                //idx++;
                dbrre.add(new DateBasedRevenueReportElement(k, mDate2Revenue.get(k)));
            }
            DateBasedRevenueReportOutputModel rs = new DateBasedRevenueReportOutputModel(dbrre);
            //rs.setRevenueElements(dbrre);
            return rs;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public SaleReportModel.Output getDateBasedSalesReport(SaleReportModel.DateBasedInput input) {
        String fromDateTime = input.getFromDate();// + " 00:00:00";
        String thruDateTime = input.getThruDate();// + " 00:00:00";
        List<SaleReportModel.DatePrice> datePrices = totalRevenueRepo.findAllByIdBetween(
                LocalDate.parse(fromDateTime, Constant.LOCAL_DATE_TIME_FORMAT),
                LocalDate.parse(thruDateTime, Constant.LOCAL_DATE_TIME_FORMAT)
        ).stream()
                .map(totalRevenue -> new SaleReportModel.DatePrice(totalRevenue.getId()
                        .format(Constant.LOCAL_DATE_FORMAT),
                        totalRevenue.getRevenue()))
                .collect(Collectors.toList());
        return new SaleReportModel.Output(datePrices);
    }
}
