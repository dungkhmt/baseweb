package com.hust.baseweb.utils;

import com.google.maps.model.LatLng;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
public class LatLngUtils {
    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(
        LatLngUtils.class);

    // Haversine formula
    // return meter
    public static int distance(double lat1, double lng1, double lat2, double lng2) {
        int r = 6_371_000;
        double dLat = deg2rad(lat2 - lat1);
        double dLng = deg2rad(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
            Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) *
                Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return (int) (r * c);
    }

    private static double deg2rad(double deg) {
        return deg * (Math.PI / 180);
    }

    public static LatLng parse(String s) {
        String[] split = s.split(",");
        return new LatLng(Double.parseDouble(split[0]), Double.parseDouble(split[1]));
    }
}
