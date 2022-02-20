package com.hust.baseweb.applications.logistics.service;

import com.hust.baseweb.applications.logistics.entity.Product;
import com.hust.baseweb.applications.logistics.model.product.ListProductsByDefinePageModel;
import com.hust.baseweb.applications.logistics.model.product.ProductByDefinePageModel;
import com.hust.baseweb.applications.logistics.model.product.ProductDetailModel;
import com.hust.baseweb.applications.logistics.model.product.UpdateProductModel;
import com.hust.baseweb.entity.UserLogin;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface ProductService {

    Product findByProductId(String productId);

    List<Product> getAllProducts();

    Product save(String productId, String productName, String uomId);

    void saveProduct(Product product);

    Product save(
        String productId,
        String productName,
        String productTransportCategory,
        double productWeight,
        String uomId,
        Integer hsThu,
        Integer hsPal
    );

    Product save(
        String productId,
        String productName,
        String type,
        String productTransportCategory,
        double productWeight,
        String uomId,
        Integer hsThu,
        Integer hsPal,
        List<String> contentIds
    );

    Product save(UserLogin u, String json, MultipartFile[] files);

    ListProductsByDefinePageModel getListProductWithPage(Pageable pageable);

    ProductDetailModel saveProductAvatar(String productId, String json, MultipartFile file);

    ProductDetailModel saveAttachmentImages(String productId, String json, MultipartFile[] files);

    ProductDetailModel getProductDetail(String productId);

    ProductDetailModel updateProduct(String productId, UpdateProductModel json);
}
