package com.hust.baseweb.applications.specialpurpose.saleslogmongo.model;

import com.hust.baseweb.applications.specialpurpose.saleslogmongo.document.OrderItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(description = "Model tạo đơn bán")
public class CreateSalesOrderInputModel {

    @ApiModelProperty(value = "user login of salesman")
    private String userLoginId;
    private Date orderDate;
    private String customerId;
    private String fromFacilityId;
    private List<OrderItem> orderItems;

}
