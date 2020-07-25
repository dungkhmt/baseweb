package com.hust.baseweb.applications.specialpurpose.saleslogmongo.document;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ApiModel("Thông tin cho biet moi product con ton 1 luong bang bao nhieu tai moi kho")
@Document("product_facility")
public class ProductFacility {
    private String productId;
    private String facilityId;
    private int quantityOnHand;// luong ton thuc su
}
