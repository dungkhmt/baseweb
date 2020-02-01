package com.hust.baseweb.applications.geo.cache;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.data.geo.Point;

public class GeoPointCache {
	private ConcurrentHashMap<String, Point> mPartyId2GeoPoint = new ConcurrentHashMap<String, Point>();
	public static final String module = GeoPointCache.class.getName();
	
	public void put(String partyId, double lat, double lng){
		System.out.println(module + "::put(" + partyId + "," + lat + "," + lng + ")");
		mPartyId2GeoPoint.put(partyId, new Point(lat, lng));
	}
	public Point getLocation(String userLoginId){
		System.out.println(module + "::getLocation(" + userLoginId + ")");
		return mPartyId2GeoPoint.get(userLoginId);
	}
}
