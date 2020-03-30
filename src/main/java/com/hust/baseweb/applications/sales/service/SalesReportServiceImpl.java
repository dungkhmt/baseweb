package com.hust.baseweb.applications.sales.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hust.baseweb.applications.order.entity.OrderHeader;
import com.hust.baseweb.applications.order.repo.OrderHeaderRepo;
import com.hust.baseweb.applications.sales.model.report.DateBasedRevenueReportElement;
import com.hust.baseweb.applications.sales.model.report.DateBasedRevenueReportOutputModel;
import com.hust.baseweb.service.UserService;
import com.hust.baseweb.utils.Constant;
import com.hust.baseweb.utils.DateTimeUtils;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Log4j2
public class SalesReportServiceImpl implements SalesReportService {
	private OrderHeaderRepo orderHeaderRepo;
	
	@Override
	public DateBasedRevenueReportOutputModel computeDateBasedRevenue(
			String fromDateStr, String toDateStr) {
		// TODO Auto-generated method stub
		try{
			Date fromDate = Constant.DATE_FORMAT.parse(fromDateStr + " 00:00:00");
			Date toDate = Constant.DATE_FORMAT.parse(toDateStr + " 23:59:59");
			HashMap<String, Double> mDate2Revenue = new HashMap<String, Double>();
			List<OrderHeader> orders = orderHeaderRepo.findAllByOrderDateBetween(fromDate, toDate);
			for(OrderHeader o: orders){
				//log.info("computeDateBasedRevenue, order " + o.getOrderId() + ", date " + o.getOrderDate() + ", revenue = " + o.getGrandTotal());
				//String yyyymmhh = o.getOrderDate().getYear() + "-" + o.getOrderDate().getMonth() + "-" + o.getOrderDate().getDate();
				String yyyyMMdd = DateTimeUtils.date2YYYYMMDD(o.getOrderDate());
				log.info("computeDateBasedRevenue, on date " + yyyyMMdd + ": has order " + o.getOrderId() + ", date " + o.getOrderDate() + ", revenue = " + o.getGrandTotal());
				if(mDate2Revenue.get(yyyyMMdd) == null){
					mDate2Revenue.put(yyyyMMdd, o.getGrandTotal());
				}else{
					mDate2Revenue.put(yyyyMMdd, mDate2Revenue.get(yyyyMMdd) + o.getGrandTotal());
				}
			}
			DateBasedRevenueReportElement[] dbrre = new DateBasedRevenueReportElement[mDate2Revenue.keySet().size()];
			int idx = -1;
			for(String k: mDate2Revenue.keySet()){
				idx++;
				dbrre[idx] = new DateBasedRevenueReportElement(k,mDate2Revenue.get(k));
			}
			DateBasedRevenueReportOutputModel rs = new DateBasedRevenueReportOutputModel();
			rs.setRevenueElements(dbrre);
			return rs;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}

}
