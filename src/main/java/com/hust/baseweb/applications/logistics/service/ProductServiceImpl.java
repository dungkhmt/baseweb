package com.hust.baseweb.applications.logistics.service;

import com.google.gson.Gson;
import com.hust.baseweb.applications.contentmanager.model.ContentHeaderModel;
import com.hust.baseweb.applications.contentmanager.model.ContentModel;
import com.hust.baseweb.applications.contentmanager.repo.MongoContentService;
import com.hust.baseweb.applications.logistics.entity.Product;
import com.hust.baseweb.applications.logistics.entity.ProductType;
import com.hust.baseweb.applications.logistics.entity.Uom;
import com.hust.baseweb.applications.logistics.model.product.CreateProductInputModel;
import com.hust.baseweb.applications.logistics.model.product.ListProductsByDefinePageModel;
import com.hust.baseweb.applications.logistics.model.product.ProductByDefinePageModel;
import com.hust.baseweb.applications.logistics.repo.ProductPagingRepo;
import com.hust.baseweb.applications.logistics.repo.ProductRepo;
import com.hust.baseweb.applications.logistics.repo.ProductTypeRepo;
import com.hust.baseweb.applications.logistics.repo.UomRepo;
import com.hust.baseweb.entity.Content;
import com.hust.baseweb.entity.UserLogin;
import com.hust.baseweb.repo.ContentRepo;
import com.hust.baseweb.service.ContentService;
import lombok.AllArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
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
    private ProductPagingRepo productPagingRepo;

    @Autowired
    private ContentService contentService;

    @Autowired
    private MongoContentService mongoContentService;

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
    public Product save(
        String productId,
        String productName,
        String productTransportCategory,
        double productWeight, // kg
        String uomId,
        Integer hsThu,
        Integer hsPal
    ) {
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
    public Product save(
        String productId,
        String productName,
        String type,
        String productTransportCategory,
        double productWeight, // kg
        String uomId,
        Integer hsThu,
        Integer hsPal,
        List<String> contentIds
    ) {
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
        Set<Content> lC = contentIds
            .stream()
            .map(id -> contentRepo.getOne(UUID.fromString(id)))
            .collect(Collectors.toSet());
        product.setContents(lC);
        product.setProductType(productType);
//        if(contentIds.size() > 0){
//            product.setAvatar(contentRepo.getOne(UUID.fromString(contentIds.get(0))));
//        }
//        UUID a = contentRepo.getOne(UUID.fromString(contentIds.get(0))).getContentId();
        if (contentIds.size() > 0) {
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
    public Product save(UserLogin u, String json, MultipartFile[] files) {
        Gson gson = new Gson();
        CreateProductInputModel input = gson.fromJson(json, CreateProductInputModel.class);
        String productId = input.getProductId();
        Product foundProduct = productRepo.findByProductId(productId);
        if (foundProduct != null) {
            throw new DuplicateKeyException("Product has been already in database");
        }
        List<String> attachmentId = new ArrayList<>();
        String[] fileId = input.getFileId();
        List<MultipartFile> fileArray = Arrays.asList(files);

        fileArray.forEach((file) -> {
            ContentModel model = new ContentModel(fileId[fileArray.indexOf(file)], file);

            ObjectId id = null;
            try {
                id = mongoContentService.storeFileToGridFs(model);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            if (id != null) {
                ContentHeaderModel rs = new ContentHeaderModel(id.toHexString());
                attachmentId.add(rs.getId());
            }
        });

        Product product = new Product();
        product.setProductId(input.getProductId());
        product.setProductName(input.getProductName());
        product.setUom(uomRepo.findByUomId(input.getUomId()));
        product.setCreatedByUserLoginId(u.getUserLoginId());
        product.setProductType(productTypeRepo.findById(input.getProductType()).orElse(null));
        product.setAvatar(String.join(";", attachmentId));
        product.setCreatedStamp(new Date());
        product = productRepo.save(product);

        return product;
    }

    @Override
    public ListProductsByDefinePageModel getListProductWithPage(Pageable pageable) {
        ListProductsByDefinePageModel products = new ListProductsByDefinePageModel();
        List<ProductByDefinePageModel> listProductResult = new ArrayList<>();

        Page<Product> productPage = productPagingRepo.findAll(pageable);
        for (Product p : productPage) {
            ProductByDefinePageModel product = new ProductByDefinePageModel();

            product.setProductId(p.getProductId());
            product.setProductName(p.getProductName());
            product.setCreatedByUserLoginId(p.getCreatedByUserLoginId());
            product.setCreatedStamp(p.getCreatedStamp());
            product.setHsPal(p.getHsPal());
            product.setHsThu(p.getHsThu());
            product.setLastUpdatedStamp(p.getLastUpdatedStamp());
            product.setProductTransportCategoryId(p.getProductTransportCategoryId());
            product.setUom(p.getUom());
            product.setWeight(p.getWeight());
            product.setContentUrls(p.getContentUrls());
            product.setContents(p.getContents());
            product.setPrimaryImg(p.getPrimaryImg());

            Uom u = p.getUom();
            if (u != null) {
//                p.setUomDescription(u.getDescription());
                product.setUomDescription(u.getDescription());
            }
//            Content content = p.getPrimaryImg();
//            if (content != null) {
//                try {
//                    Response response = contentService.getContentData(content.getContentId().toString());
//                    String base64Flag = "data:image/jpeg;base64," +
//                                        Base64.getEncoder().encodeToString(response.body().bytes());
//                    p.setAvatar(base64Flag);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
            if (p.getAvatar() != null) {
                String fileId = p.getAvatar();
                    try {
                        GridFsResource contentFile = mongoContentService.getById(fileId);
                        if (contentFile != null) {
                            InputStream inputStream = contentFile.getInputStream();
                            product.setAvatar(IOUtils.toByteArray(inputStream));
                        }
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

            }

            listProductResult.add(product);
        }

        products.setProducts(listProductResult);
        products.setTotalElements(productPage.getTotalElements());

        return products;
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
