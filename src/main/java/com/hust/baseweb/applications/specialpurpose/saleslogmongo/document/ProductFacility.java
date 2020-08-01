package com.hust.baseweb.applications.specialpurpose.saleslogmongo.document;

import io.swagger.annotations.ApiModel;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ApiModel("Thông tin cho biet moi product con ton 1 luong bang bao nhieu tai moi kho")
@Document("product_facility")
public class ProductFacility {

    @Id
    private ProductFacilityId id;
    private int quantityOnHand;// luong ton thuc su

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @EqualsAndHashCode
    public static class ProductFacilityId {

        private String productId;
        private String facilityId;
    }
}
