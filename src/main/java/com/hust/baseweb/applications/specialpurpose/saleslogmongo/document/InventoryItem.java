package com.hust.baseweb.applications.specialpurpose.saleslogmongo.document;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document("inventory_items")
@ApiModel(description = "Ghi chép nhập kho")
public class InventoryItem {

    @Id
    private ObjectId inventoryItemId;

    @Indexed
    private String productId;

    @Indexed
    private String facilityId;

    private Integer quantityOnHandTotal;
}
