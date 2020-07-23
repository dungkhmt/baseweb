package com.hust.baseweb.applications.gismap.document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import org.springframework.data.annotation.Id;
import java.util.ArrayList;
import java.util.Date;
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
    private List<Point> points;

    public void addPoint(double lat, double lng, String timestamp){
        Point p = new Point();
        p.setLat(lat); p.setLng(lng); p.setTimestamp(timestamp);
        if(points == null) points = new ArrayList<>();
        points.add(p);
    }
}
