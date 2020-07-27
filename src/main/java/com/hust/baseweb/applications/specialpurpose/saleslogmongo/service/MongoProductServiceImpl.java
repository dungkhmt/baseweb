package com.hust.baseweb.applications.specialpurpose.saleslogmongo.service;

import com.hust.baseweb.applications.specialpurpose.saleslogmongo.document.Product;
import com.hust.baseweb.applications.specialpurpose.saleslogmongo.model.ProductModel;
import com.hust.baseweb.applications.specialpurpose.saleslogmongo.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class MongoProductServiceImpl implements MongoProductService {
    private final ProductRepository productRepository;
    @Override
    public List<ProductModel> findAllProductAndPrice() {
        List<Product> products = productRepository.findAll();
        List<ProductModel> productModels = new ArrayList<ProductModel>();
        for(Product p: products){
            // temporarily hard code
            productModels.add(new ProductModel(p.getProductId(),p.getProductName(),1000));
        }
        return productModels;
    }
}
