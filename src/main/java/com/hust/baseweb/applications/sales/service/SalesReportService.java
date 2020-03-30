package com.hust.baseweb.applications.sales.service;

import org.springframework.stereotype.Service;

import com.hust.baseweb.applications.sales.model.report.DateBasedRevenueReportOutputModel;

@Service
public interface SalesReportService {
	public DateBasedRevenueReportOutputModel computeDateBasedRevenue(String fromDate, String toDate);
}
