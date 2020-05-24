package com.hust.baseweb.applications.logistics.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ListProductImageModel {
    List<ProductImageInfoModel> productImageInfoModels;
    String primaryImgId;

}
