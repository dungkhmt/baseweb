package com.hust.baseweb.applications.specialpurpose.saleslogmongo.document;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ApiModel(description = "Ghi chép xuất kho")
public class InventoryItemDetail {

    @Id
    private ObjectId inventoryItemDetailId;

    @Indexed
    @ApiModelProperty(value = "Id của ghi chép nhập kho tương ứng")
    private ObjectId inventoryItemId;

    private Date effectiveDate;

    private Integer quantityOnHandDiff;

    private String orderId;

    private ObjectId orderItemId;
}
