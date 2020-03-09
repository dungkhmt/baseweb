package com.hust.baseweb.applications.logistics.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ModelCreateProductInput {
    private String productId;
    private String quantityUomId;
    private String type;
    private String productName;

    public ModelCreateProductInput(String productId, String quantityUomId, String type, String productName) {
        this.productId = productId;
        this.quantityUomId = quantityUomId;
        this.type = type;
        this.productName = productName;
    }

    @Override
    public String toString() {
        return "ModelCreateProductInput{" +
                "productId='" + productId + '\'' +
                ", quantityUomId='" + quantityUomId + '\'' +
                ", type='" + type + '\'' +
                ", productName='" + productName + '\'' +
                '}';
    }
}
