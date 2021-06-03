package com.hust.baseweb.applications.specialpurpose.saleslogmongo.model;

import com.hust.baseweb.applications.specialpurpose.saleslogmongo.document.PurchaseOrder;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ApiModel(description = "Model tạo đơn mua")
public class CreatePurchaseOrderInputModel {
    public static final SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private String fromProviderId;
    private String userLoginId;
    private String orderDate;
    private String toFacilityId;
    private List<OrderItemModel> orderItems; // product-quantity

    public PurchaseOrder toPurchaseOrder() {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setFromProviderOrganizationId(fromProviderId);
        purchaseOrder.setPoStaffId(userLoginId);
        try {
            purchaseOrder.setOrderDate(datetimeFormat.parse(orderDate));
        }catch(Exception e){
            e.printStackTrace();
        }
        purchaseOrder.setToFacilityId(toFacilityId);

        return purchaseOrder;
    }
}
