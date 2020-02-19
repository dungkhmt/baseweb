package com.hust.baseweb.applications.tracklocations.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter

public class PostLocationInputModel {
    private double lat;
    private double lng;
    private Date timePoint;

    public PostLocationInputModel() {
        super();

    }

}
