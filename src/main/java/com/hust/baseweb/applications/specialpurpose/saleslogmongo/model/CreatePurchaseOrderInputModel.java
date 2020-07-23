package com.hust.baseweb.applications.specialpurpose.saleslogmongo.model;

import com.hust.baseweb.applications.specialpurpose.saleslogmongo.document.PurchaseOrder;
import io.swagger.annotations.ApiModel;
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
@ApiModel(description = "Model tạo đơn mua")
public class CreatePurchaseOrderInputModel {

    private String fromProviderId;
    private String userLoginId;
    private Date orderDate;
    private String toFacilityId;
    private List<OrderItemModel> orderItems; // product-quantity

    public PurchaseOrder toPurchaseOrder() {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setFromProviderOrganizationId(fromProviderId);
        purchaseOrder.setPoStaffId(new ObjectId(userLoginId));
        purchaseOrder.setOrderDate(orderDate);
        purchaseOrder.setToFacilityId(toFacilityId);

        return purchaseOrder;
    }
}
