package com.hust.baseweb.applications.postsys.model.posttrip;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExecuteTripInputModel {
    private String postOfficeFixedTripId;
    private String postOfficeFixedTripExecuteId;
    private Date depatureDateTime;
    private String postmanId;
    private String status;
    private List<String> postShipOrderTripPostOfficeAssignmentIds;

}
