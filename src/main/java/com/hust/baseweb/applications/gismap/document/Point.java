package com.hust.baseweb.applications.gismap.document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document
public class Point {
    private String pointId;
    private double lat;
    private double lng;
    private String timestamp;
}
