package com.hust.baseweb.applications.specialpurpose.saleslogmongo.document;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ApiModel("Sản phẩm")
@Document("products")
public class Product {

    @Id
    private String productId;
    private String productName;

}
