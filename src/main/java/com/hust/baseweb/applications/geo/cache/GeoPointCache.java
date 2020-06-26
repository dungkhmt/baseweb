package com.hust.baseweb.applications.geo.cache;

import org.springframework.data.geo.Point;

import java.util.concurrent.ConcurrentHashMap;

public class GeoPointCache {

    public static final String module = GeoPointCache.class.getName();
    private ConcurrentHashMap<String, Point> mapPartyId2GeoPoint = new ConcurrentHashMap<String, Point>();

    public void put(String partyId, double lat, double lng) {
        System.out.println(module + "::put(" + partyId + "," + lat + "," + lng + ")");
        mapPartyId2GeoPoint.put(partyId, new Point(lat, lng));
    }

    public Point getLocation(String userLoginId) {
        System.out.println(module + "::getLocation(" + userLoginId + ")");
        return mapPartyId2GeoPoint.get(userLoginId);
    }
}
