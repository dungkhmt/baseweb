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
@ApiModel("Giá sản phẩm")
public class ProductPrice {

    @Id
    private ObjectId id;

    private String productId;
    private Long unitPrice;

    private Date fromDate;
    private Date thruDate;
}
