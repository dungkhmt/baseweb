package com.hust.baseweb.applications.sales.service;

import com.hust.baseweb.applications.logistics.model.product.SaleReportModel;
import org.springframework.stereotype.Service;

import com.hust.baseweb.applications.sales.model.report.DateBasedRevenueReportOutputModel;

@Service
public interface SalesReportService {
	public DateBasedRevenueReportOutputModel computeDateBasedRevenue(String fromDate, String toDate);

	public SaleReportModel.Output getSaleReports(SaleReportModel.Input input);

	public SaleReportModel.Output getDateBasedSalesReport(SaleReportModel.DateBasedInput input );
}
