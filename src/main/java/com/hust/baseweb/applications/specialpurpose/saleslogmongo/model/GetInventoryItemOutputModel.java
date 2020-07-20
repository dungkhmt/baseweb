package com.hust.baseweb.applications.specialpurpose.saleslogmongo.model;

import com.hust.baseweb.applications.specialpurpose.saleslogmongo.document.Facility;
import com.hust.baseweb.applications.specialpurpose.saleslogmongo.document.Product;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ApiModel(description = "Model danh sách tồn kho")
public class GetInventoryItemOutputModel {

    private Facility facility;

    private List<ProductQuantity> productQuantities;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class ProductQuantity {

        private Product product;
        private int quantity;
    }
}
