package com.hust.baseweb.applications.specialpurpose.saleslogmongo.document;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ApiModel("Thông tin đơn hàng bán")
public class SalesOrder {

    @Id
    private ObjectId id;

    private String orderId;
    private Organization customer;
    private Date orderDate;
    private String salesmanId; // map to Person
    private String fromFacilityId;
    private List<OrderItem> orderItems;

}
