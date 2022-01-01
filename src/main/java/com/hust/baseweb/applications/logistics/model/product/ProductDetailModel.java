package com.hust.baseweb.applications.logistics.model.product;

import com.hust.baseweb.applications.logistics.entity.Product;
import com.hust.baseweb.constant.ContentMappingConstant;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
@ToString
public class ProductDetailModel {

    private String productId;
    private String productName;
    private String type;
    private String createdByUserLoginId;
    private String uom;
    private List<String> contentUrls;
    private byte[] avatar;
    private List<byte[]> attachmentImages;
    private Integer hsThu;
    private Integer hsPal;
    private String productTransportCategoryId;
    private Double weight;
    private String description;

    private List<ProductPromoModel> productPromoModels;

    public ProductDetailModel() {
    }

    public ProductDetailModel(Product product) {
//        product.setProductId(p.getProductId());
//        product.setProductName(p.getProductName());
//        product.setCreatedByUserLoginId(p.getCreatedByUserLoginId());
//        product.setCreatedStamp(p.getCreatedStamp());
//        product.setHsPal(p.getHsPal());
//        product.setHsThu(p.getHsThu());
//        product.setLastUpdatedStamp(p.getLastUpdatedStamp());
//        product.setProductTransportCategoryId(p.getProductTransportCategoryId());
//        product.setUom(p.getUom());
//        product.setWeight(p.getWeight());
//        product.setContentUrls(p.getContentUrls());
//        product.setContents(p.getContents());
//        product.setPrimaryImg(p.getPrimaryImg());
        this.productId = product.getProductId();
        this.productName = product.getProductName();
        this.type = product.getProductType() == null ? "UNKNOWN" : product.getProductType().getDescription();
        this.uom = product.getUom() == null ? "UNKNOWN" : product.getUom().getDescription();
        this.createdByUserLoginId = product.getCreatedByUserLoginId();
        this.hsPal = product.getHsPal();
        this.hsThu = product.getHsThu() ;
        this.productTransportCategoryId = product.getProductTransportCategoryId();
        this.weight = product.getWeight();
        this.description = product.getDescription();

//        this.contentUrls = product.getContents().stream().map(content -> {
//            return ContentMappingConstant.CONTENT_MAPPING + "/" + content.getContentId().toString();
//        }).collect(Collectors.toList());

//        this.avatar = product.getAvatar();
    }

}
