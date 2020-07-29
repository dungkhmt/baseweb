package com.hust.baseweb.applications.specialpurpose.saleslogmongo.document;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ApiModel("Thông tin đơn hàng mua")
@Document("purchase_orders")
public class PurchaseOrder {

    @Id
    private ObjectId purchaseOrderId;

    private String fromProviderOrganizationId; // map to Organization
    private Date orderDate;
    private String poStaffId;
    private String toFacilityId;

    //private List<ObjectId> orderItemIds;
    private List<OrderItem> orderItems;
}
