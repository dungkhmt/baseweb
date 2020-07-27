package com.hust.baseweb.applications.specialpurpose.saleslogmongo.model;

import com.hust.baseweb.applications.specialpurpose.saleslogmongo.document.SalesOrder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ApiModel(description = "Model tạo đơn bán")
public class CreateSalesOrderInputModel {

    private String customerId;
    private String orderDate;
    @ApiModelProperty(value = "user login of salesman")
    private String userLoginId;
    private String fromFacilityId;

    private List<OrderItemModel> orderItems;

    public SalesOrder toSalesOrder() {
        SalesOrder salesOrder = new SalesOrder();
        salesOrder.setCustomerOrganizationId(customerId);
        salesOrder.setOrderDate(orderDate);
        salesOrder.setSalesmanId(new ObjectId(userLoginId));
        salesOrder.setFromFacilityId(fromFacilityId);

        return salesOrder;
    }
}
