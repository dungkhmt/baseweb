package com.hust.baseweb.applications.tms.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfigParam {
    private double maxDistanceConsecutiveLocationTripAllowed;// do dai quang duong toi da cho phep giua 2 location
                                                            // lien tiep trong moi trip

    private int maxLocationsPerTripAllowed;// so locations toi da tren moi trip
    
    private int fixLoadTime;// thoi gian co dinh load hang
    private int fixUnloadTime;// thoi gian co dinh unload hang
    private double load_rate;// he so thoi gian dong load hang (per unit kg)
    private double unload_rate;// he so thoi gian unload hang (per unit kg)
    /*
    tong thoi gian phuc vu load hang tai moi diem = fixLoadTime + load_rate * kg
    tong thoi gian phuc vu unload hang tai moi diem = fixUnloadTime + unload_rate * kg
     */
}
