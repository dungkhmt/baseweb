package com.hust.baseweb.applications.postsys.model.posttrip;

import com.hust.baseweb.applications.postsys.entity.PostShipOrderFixedTripPostOfficeAssignment;
import com.hust.baseweb.applications.postsys.entity.Postman;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

public interface ExecuteTripOutputModelOM {
    String getPostOfficeFixedTripExecuteId();
    String getPostOfficeFixedTripId();
    Date getDepatureDateTime();
    Postman getPostman();
    String getStatus();
    List<PostShipOrderFixedTripPostOfficeAssignment> getPostShipOrderFixedTripPostOfficeAssignments();
}
