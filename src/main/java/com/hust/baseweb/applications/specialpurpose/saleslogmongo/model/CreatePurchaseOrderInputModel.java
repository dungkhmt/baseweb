package com.hust.baseweb.applications.specialpurpose.saleslogmongo.model;

import com.hust.baseweb.applications.specialpurpose.saleslogmongo.document.OrderItem;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ApiModel(description = "Model tạo đơn mua")
public class CreatePurchaseOrderInputModel {

    private String fromProviderId;
    private Date orderDate;
    private String userLoginId;
    private String toFacilityId;
    private List<OrderItem> orderItem;
}
