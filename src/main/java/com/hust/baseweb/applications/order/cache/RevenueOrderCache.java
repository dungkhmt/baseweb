package com.hust.baseweb.applications.order.cache;

import com.hust.baseweb.applications.order.entity.report.RPTDayRevenue;

import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;


public class RevenueOrderCache {

    public static final String module = RevenueOrderCache.class.getName();

    private ConcurrentHashMap<String, RPTDayRevenue> mDate2Revenue = new ConcurrentHashMap<>();

    public synchronized void addOrderRevenue(String date, Double revenue) {
        //System.out.println(module + "::addOrderRevenue(" + date + "," + revenue);

        if (mDate2Revenue.get(date) == null) {
            RPTDayRevenue rptDayRevenue = new RPTDayRevenue(date, revenue, 1);
            mDate2Revenue.put(date, rptDayRevenue);
        } else {
            RPTDayRevenue e = mDate2Revenue.get(date);
            e.setRevenue(e.getRevenue() + revenue);
            e.setNumberOrders(e.getNumberOrders() + 1);

        }
    }

    public Enumeration<String> keys() {

        return mDate2Revenue.keys();
    }

    public Double getRevenue(String date) {

        return mDate2Revenue.get(date).getRevenue();
    }
}
