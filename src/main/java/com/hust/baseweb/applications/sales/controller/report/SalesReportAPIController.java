package com.hust.baseweb.applications.sales.controller.report;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.hust.baseweb.applications.logistics.model.product.SaleReportModel;
import com.hust.baseweb.applications.sales.model.report.DateBasedRevenueReportElement;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hust.baseweb.applications.customer.repo.CustomerRepo;
import com.hust.baseweb.applications.customer.repo.DistributorRepo;
import com.hust.baseweb.applications.customer.service.CustomerService;
import com.hust.baseweb.applications.customer.service.DistributorService;
import com.hust.baseweb.applications.sales.model.report.DateBasedRevenueReportInputModel;
import com.hust.baseweb.applications.sales.model.report.DateBasedRevenueReportOutputModel;
import com.hust.baseweb.applications.sales.model.report.DateBasedRevenueReportRecentInputModel;
import com.hust.baseweb.applications.sales.service.SalesReportService;
import com.hust.baseweb.entity.UserLogin;
import com.hust.baseweb.service.UserService;
import com.hust.baseweb.utils.DateTimeUtils;

@RestController
@Log4j2
@CrossOrigin
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class SalesReportAPIController {
	private UserService userService;
	private SalesReportService salesReportService;
	
	@PostMapping("/report-date-based-revenue")
	public ResponseEntity<?> reportDateBasedRevenue(Principal principal, @RequestBody DateBasedRevenueReportInputModel input){
		UserLogin u = userService.findById(principal.getName());
	
		DateBasedRevenueReportOutputModel revenueReport = salesReportService.computeDateBasedRevenue(input.getFromDate(), input.getToDate());
		
		return ResponseEntity.ok().body(revenueReport);		
	}

	@PostMapping("/get-report-date-based-revenue")
	public ResponseEntity<?> getReportDateBasedRevenue(Principal principal, @RequestBody SaleReportModel.DateBasedInput input){
		SaleReportModel.Output output = salesReportService.getDateBasedSalesReport(input);
		return ResponseEntity.ok().body(output);
	}
	
	@PostMapping("/report-date-based-revenue-recent")
	public ResponseEntity<?> reportDateBasedRevenueRecent(Principal principal, @RequestBody DateBasedRevenueReportRecentInputModel input){
		UserLogin u = userService.findById(principal.getName());
		String fromDateStr = "";
		String toDateStr = "";
		Date currentDate = new Date();
		toDateStr = DateTimeUtils.date2YYYYMMDD(currentDate);
		int cnt = 1;
		while(cnt <= input.getNbDays()-1){
			currentDate = DateTimeUtils.next(currentDate,-1);
			cnt++;
		}
		fromDateStr = DateTimeUtils.date2YYYYMMDD(currentDate);
		log.info("reportDateBasedRevenueRecent, fromDate = " + fromDateStr + ", toDate = " + toDateStr);
		//DateBasedRevenueReportOutputModel revenueReport = salesReportService.computeDateBasedRevenue(fromDateStr, toDateStr);

		SaleReportModel.DateBasedInput I = new SaleReportModel.DateBasedInput(fromDateStr + " 00:00:00", toDateStr + " 23:59:59");
		SaleReportModel.Output output = salesReportService.getDateBasedSalesReport(I);
		List<DateBasedRevenueReportElement> lst = output.getDatePrices().stream().map(e -> new DateBasedRevenueReportElement(e.getDate(),e.getPrice()))
				.collect(Collectors.toList());
		DateBasedRevenueReportOutputModel revenueReport = new DateBasedRevenueReportOutputModel(lst);
		return ResponseEntity.ok().body(revenueReport);		
	}

	@PostMapping("/get-sale-reports")
	public ResponseEntity<?> getSaleReports(@RequestBody SaleReportModel.Input input) {
		return ResponseEntity.ok().body(salesReportService.getSaleReports(input));
	}
}
