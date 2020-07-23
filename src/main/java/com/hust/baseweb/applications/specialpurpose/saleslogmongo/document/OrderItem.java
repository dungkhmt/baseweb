package com.hust.baseweb.applications.specialpurpose.saleslogmongo.document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document("order_items")
public class OrderItem {

    @Id
    private ObjectId orderItemId;

    private String productId;
    private Integer quantity;
}
