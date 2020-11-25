package com.hust.baseweb.applications.postsys.model.posttrip;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
public class CreatePostTripModel {
    private String fromPostOfficeId;
    private String toPostOfficeId;
    private Date sheduleDepatureTime;
    private Date fromDate;
    private Date thruDate;
}
