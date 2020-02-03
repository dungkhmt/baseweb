package com.hust.baseweb.applications.order.cache;

import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

import com.hust.baseweb.applications.order.entity.report.RPTDayRevenue;



public class RevenueOrderCache {
	public static final String module = RevenueOrderCache.class.getName();
	
	private ConcurrentHashMap<String, RPTDayRevenue> mDate2Revenue = new ConcurrentHashMap<String, RPTDayRevenue>();
	
	public synchronized void addOrderRevenue(String date, BigDecimal revenue){
		//System.out.println(module + "::addOrderRevenue(" + date + "," + revenue);
		
		if(mDate2Revenue.get(date) == null){
			RPTDayRevenue e = new RPTDayRevenue(date, revenue, 1);
			mDate2Revenue.put(date, e);
		}else{
			RPTDayRevenue e = mDate2Revenue.get(date);
			e.setRevenue(e.getRevenue().add(revenue));
			e.setNumberOrders(e.getNumberOrders() + 1);
			
		}
	}
	public Enumeration<String> keys(){
		return mDate2Revenue.keys();
	}
	public BigDecimal getRevenue(String date){
		return mDate2Revenue.get(date).getRevenue();
	}
}
