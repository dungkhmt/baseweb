package com.hust.baseweb.applications.specialpurpose.saleslogmongo.document;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ApiModel("Thông tin đơn hàng mua")
public class PurchaseOrder {

    @Id
    private String orderId;

    private String fromProviderId; // map to Organization
    private ObjectId poStaffId;
    private Date orderDate;
    private String toFacilityId;
    private ObjectId[] orderItemIds;
}
