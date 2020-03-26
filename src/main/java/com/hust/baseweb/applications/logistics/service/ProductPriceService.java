package com.hust.baseweb.applications.logistics.service;

import com.hust.baseweb.applications.logistics.entity.ProductPrice;
import com.hust.baseweb.applications.logistics.model.product.SaleReportModel;
import com.hust.baseweb.entity.UserLogin;
import org.springframework.stereotype.Service;

import java.text.ParseException;


@Service
public interface ProductPriceService {
    ProductPrice setProductPrice(UserLogin createdByUserLogin,
                                 String productId,
                                 Double price,
                                 String currencyUomId,
                                 String taxInPrice);

    ProductPrice getProductPrice(String productId);

    SaleReportModel.Output getSaleReports(SaleReportModel.Input input) throws ParseException;
}
