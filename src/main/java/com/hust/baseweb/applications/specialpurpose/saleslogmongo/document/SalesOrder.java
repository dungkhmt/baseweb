package com.hust.baseweb.applications.specialpurpose.saleslogmongo.document;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ApiModel("Thông tin đơn hàng bán")
@Document("sales_orders")
public class SalesOrder {

    @Id
    private ObjectId salesOrderId;

    private String customerOrganizationId;
    private String orderDate;
    private String salesmanId; // userlogin of salesman
    private String fromFacilityId;

    //private List<ObjectId> orderItemIds;
    private List<OrderItem> orderItems;
}
