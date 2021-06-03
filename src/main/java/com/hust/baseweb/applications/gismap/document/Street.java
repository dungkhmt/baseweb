package com.hust.baseweb.applications.gismap.document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document
public class Street {
    @Id
    private String streetId;
    private String streetName;
    private boolean forCar;
    private boolean forTruck;
    private boolean forMotobike;
    private boolean directional;// one-way
    private List<Point> points;

    private String createdByUserLoginId;

    public void addPoint(double lat, double lng, String timestamp){
        Point p = new Point();
        p.setLat(lat); p.setLng(lng); p.setTimestamp(timestamp);
        if(points == null) points = new ArrayList<>();
        points.add(p);
    }
}
