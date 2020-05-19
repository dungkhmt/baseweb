package com.hust.baseweb.applications.logistics.model.product;

import java.util.List;
import java.util.stream.Collectors;

import com.hust.baseweb.applications.logistics.entity.Product;
import com.hust.baseweb.constant.ContentMappingConstant;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ProductDetailModel {
    private String productId;
    private String productName;
    private String type;
    private String uom;
    private List<String> contentUrls;
    private String avatar;

    public ProductDetailModel() {
    }

    public ProductDetailModel(Product product) {
        this.productId = product.getProductId();
        this.productName = product.getProductName();
        this.type = product.getProductType() == null ? "UNKNOWN" : product.getProductType().getDescription();
        this.uom = product.getUom() == null ? "UNKNOWN" : product.getUom().getDescription();

        this.contentUrls = product.getContents().stream().map(content -> {
            return   ContentMappingConstant.CONTENT_MAPPING +"/"+ content.getContentId().toString();
        }).collect(Collectors.toList());

        this.avatar = product.getAvatar();
    }

}