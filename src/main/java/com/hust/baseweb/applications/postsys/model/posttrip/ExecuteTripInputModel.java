package com.hust.baseweb.applications.postsys.model.posttrip;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExecuteTripInputModel {
    private String postOfficeFixedTripExecuteId;
    private String postOfficeFixedTripId;
    private Date depatureDateTime;
    private String postmanId;
    private String status;
}
