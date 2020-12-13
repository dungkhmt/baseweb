package com.hust.baseweb.applications.postsys.model.posttrip;

import com.hust.baseweb.applications.postsys.entity.PostOrder;
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
public class ExecuteTripOutputModel {
    private String postOfficeFixedTripExecuteId;
    private String postOfficeFixedTripId;
    private Date depatureDateTime;
    private String postmanName;
    private String status;
    private List<PostOrder> postOrders;
}
