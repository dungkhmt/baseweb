package com.hust.baseweb.applications.logistics.service;

import com.hust.baseweb.applications.logistics.entity.ProductPrice;
import com.hust.baseweb.entity.UserLogin;
import org.springframework.stereotype.Service;


@Service
public interface ProductPriceService {
    ProductPrice setProductPrice(UserLogin createdByUserLogin,
                                 String productId,
                                 Double price,
                                 String currencyUomId,
                                 String taxInPrice);

    ProductPrice getProductPrice(String productId);
}
