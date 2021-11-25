package com.hust.baseweb.applications.logistics.model.product;

import com.hust.baseweb.applications.logistics.entity.ProductType;
import com.hust.baseweb.applications.logistics.entity.Uom;
import com.hust.baseweb.entity.Content;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class ProductByDefinePageModel {
    private String productId;
    private String productName;
    private Double weight;
    private Uom uom;
    private ProductType productType;
    private String productTransportCategoryId;
    private Date createdStamp;
    private Date lastUpdatedStamp;
    private String uomDescription;
    private String productTypeDescription;
    private Integer hsThu;
    private Integer hsPal;
    private Set<Content> contents;
    private String createdByUserLoginId;
    private List<String> contentUrls;
    private Content primaryImg;
    private byte[] avatar;
}
