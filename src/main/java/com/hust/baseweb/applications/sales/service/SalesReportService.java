package com.hust.baseweb.applications.sales.service;

import com.hust.baseweb.applications.logistics.model.product.SaleReportModel;
import com.hust.baseweb.applications.sales.model.report.DateBasedRevenueReportOutputModel;
import org.springframework.stereotype.Service;

@Service
public interface SalesReportService {
    DateBasedRevenueReportOutputModel computeDateBasedRevenue(String fromDate, String toDate);

    SaleReportModel.Output getSaleReports(SaleReportModel.Input input);

    SaleReportModel.Output getDateBasedSalesReport(SaleReportModel.DateBasedInput input);
}
