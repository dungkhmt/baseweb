package com.hust.baseweb.applications.logistics.service;

import com.hust.baseweb.applications.logistics.entity.Product;
import com.hust.baseweb.applications.logistics.entity.ProductType;
import com.hust.baseweb.applications.logistics.entity.Uom;
import com.hust.baseweb.applications.logistics.repo.ProductRepo;
import com.hust.baseweb.applications.logistics.repo.ProductTypeRepo;
import com.hust.baseweb.applications.logistics.repo.UomRepo;
import com.hust.baseweb.entity.Content;
import com.hust.baseweb.repo.ContentRepo;
import com.hust.baseweb.repo.FileRepo;
import com.hust.baseweb.service.ContentService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import okhttp3.Response;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ProductServiceImpl implements ProductService {
    private ProductRepo productRepo;
    private UomRepo uomRepo;
    private UomService uomService;
    private ContentRepo contentRepo;
    private ProductTypeRepo productTypeRepo;

    @Autowired
    private ContentService contentService;


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
    public Product save(String productId,
                        String productName,
                        String productTransportCategory,
                        double productWeight, // kg
                        String uomId,
                        Integer hsThu,
                        Integer hsPal) {
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
        product.setHsThu(hsThu);
        product.setHsPal(hsPal);
        product = productRepo.save(product);
        return product;
    }
    @Override
    @Transactional
    public Product save(String productId,
                        String productName,
                        String type,
                        String productTransportCategory,
                        double productWeight, // kg
                        String uomId,
                        Integer hsThu,
                        Integer hsPal,
                        List<String> contentIds) {
        // TODO: check duplicate productId
        Uom uom = uomRepo.findByUomId(uomId);
        if (uom == null) {
            uom = uomService.save(uomId, "UNIT_MEASURE", uomId, uomId);
        }
        ProductType productType = productTypeRepo.findById(type).orElseThrow(NoSuchElementException::new);

        Product product = new Product();
        product.setProductName(productName);
        product.setProductId(productId);
        product.setWeight(productWeight);
        product.setUom(uom);
        product.setProductTransportCategoryId(productTransportCategory);
        product.setHsThu(hsThu);
        product.setHsPal(hsPal);
        Set<Content> lC = contentIds.stream()
                .map(id -> contentRepo.getOne(UUID.fromString(id)))
                .collect(Collectors.toSet());
        product.setContents(lC);
        product.setProductType(productType);
//        if(contentIds.size() > 0){
//            product.setAvatar(contentRepo.getOne(UUID.fromString(contentIds.get(0))));
//        }
//        UUID a = contentRepo.getOne(UUID.fromString(contentIds.get(0))).getContentId();
        if(contentIds.size() > 0){
            String avatarId = contentIds.get(0);
            Content content = contentRepo.findByContentId(UUID.fromString(avatarId));
            product.setPrimaryImg(content);

//            try {
//                Response response = contentService.getContentData(avatarId);
//                String base64Flag = "data:image/jpeg;base64,"+ Base64.getEncoder().encodeToString(response.body().bytes());
//                product.setAvatar(base64Flag);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }



        product = productRepo.save(product);
        return product;
    }
    @Override
    public void saveProduct(Product product) {
        productRepo.save(product);
    }


    @Override
    @Transactional
    public Product save(String productId, String productName, String uomId) {
        // TODO: check duplicate productId
        Uom uom = uomRepo.findByUomId(uomId);
        if (uom == null) {
            uom = uomService.save(uomId, "UNIT_MEASURE", uomId, uomId);
        }
        Product product = new Product();
        product.setProductName(productName);
        product.setProductId(productId);
        product.setUom(uom);
        product = productRepo.save(product);
        return product;
    }

}
