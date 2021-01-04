package com.hust.baseweb.applications.postsys.model.posttrip;

import com.hust.baseweb.applications.postsys.entity.PostShipOrderFixedTripPostOfficeAssignment;
import com.hust.baseweb.applications.postsys.entity.Postman;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface ExecuteTripOutputModelOM {
    String getPostOfficeFixedTripExecuteId();
    String getPostOfficeFixedTripId();
    String getStatus();
}
