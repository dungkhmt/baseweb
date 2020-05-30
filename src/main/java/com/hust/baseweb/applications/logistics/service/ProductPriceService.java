package com.hust.baseweb.applications.logistics.service;

import com.hust.baseweb.applications.logistics.entity.ProductPrice;
import com.hust.baseweb.entity.UserLogin;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface ProductPriceService {

    ProductPrice setProductPrice(
        UserLogin createdByUserLogin,
        String productId,
        Double price,
        String currencyUomId,
        String taxInPrice, String fromDate, String thruDate
    );

    ProductPrice getProductPrice(String productId);

    List<ProductPrice.Model> getProductPriceHistory(String productId);

    //SaleReportModel.Output getSaleReports(SaleReportModel.Input input);
}
