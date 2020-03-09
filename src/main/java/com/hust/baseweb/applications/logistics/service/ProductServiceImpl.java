package com.hust.baseweb.applications.logistics.service;

import com.hust.baseweb.applications.logistics.entity.Product;
import com.hust.baseweb.applications.logistics.entity.Uom;
import com.hust.baseweb.applications.logistics.repo.ProductRepo;
import com.hust.baseweb.applications.logistics.repo.UomRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ProductServiceImpl implements ProductService {
    private ProductRepo productRepo;
    private UomRepo uomRepo;
    private UomService uomService;

    @Override
    public Product findByProductId(String productId) {

        return productRepo.findByProductId(productId);
    }

    @Override
    public List<Product> getAllProducts() {

        return productRepo.findAll();
    }

    @Override
    @Transactional
    public Product save(String productId, String productName, String productTransportCategory, double productWeight, String uomId) {
        // TODO: check duplicate productId
        Uom uom = uomRepo.findByUomId(uomId);
        if (uom == null) {
            uom = uomService.save(uomId, "UNIT_MEASURE", uomId, uomId);
        }
        Product product = new Product();
        product.setProductName(productName);
        product.setProductId(productId);
        product.setWeight(productWeight);
        product.setUom(uom);
        product.setProductTransportCategoryId(productTransportCategory);
        product = productRepo.save(product);
        return product;
    }

}
