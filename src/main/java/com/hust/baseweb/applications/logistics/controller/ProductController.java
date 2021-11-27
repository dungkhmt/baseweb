package com.hust.baseweb.applications.logistics.controller;

import com.hust.baseweb.applications.logistics.entity.Product;
import com.hust.baseweb.applications.logistics.entity.ProductType;
import com.hust.baseweb.applications.logistics.entity.Uom;
import com.hust.baseweb.applications.logistics.model.*;
import com.hust.baseweb.applications.logistics.model.product.ListProductsByDefinePageModel;
import com.hust.baseweb.applications.logistics.model.product.ProductByDefinePageModel;
import com.hust.baseweb.applications.logistics.model.product.ProductDetailModel;
import com.hust.baseweb.applications.logistics.repo.ProductPagingRepo;
import com.hust.baseweb.applications.logistics.repo.ProductRepo;
import com.hust.baseweb.applications.logistics.repo.ProductTypeRepo;
import com.hust.baseweb.applications.logistics.service.ProductService;
import com.hust.baseweb.applications.logistics.service.ProductTypeService;
import com.hust.baseweb.applications.logistics.service.UomService;
import com.hust.baseweb.entity.Content;
import com.hust.baseweb.entity.UserLogin;
import com.hust.baseweb.repo.ContentRepo;
import com.hust.baseweb.service.ContentService;
import com.hust.baseweb.service.UserService;
import lombok.extern.log4j.Log4j2;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@Log4j2

public class ProductController {

    private UomService uomService;
    private ProductTypeService productTypeService;
    private ProductService productService;
    private ProductTypeRepo productTypeRepo;
    private ProductPagingRepo productPagingRepo;

    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private ContentService contentService;

    @Autowired
    private UserService userService;

    @Autowired
    private ContentRepo contentRepo;


    @Autowired
    ProductController(
        UomService uomService, ProductTypeService productTypeService, ProductService productService,
        ProductTypeRepo productTypeRepo, ProductPagingRepo productPagingRepo
    ) {
        this.uomService = uomService;
        this.productTypeService = productTypeService;
        this.productService = productService;
        this.productTypeRepo = productTypeRepo;
        this.productPagingRepo = productPagingRepo;
    }

    @PostMapping("/get-list-uoms")
    public ResponseEntity<?> getListUoms(Principal principal, @RequestBody InputModel input) {
        log.info("getListUoms {}", input.getStatusId());
        List<Uom> uoms = uomService.getAllUoms();
        log.info("uoms size: {}", uoms.size());

        return ResponseEntity.ok().body(new GetListUomOutputModel(uoms));

    }

    @PostMapping("/get-list-product-type")
    public ResponseEntity<?> getListProductType(Principal principal, @RequestBody InputModel input) {
        log.info("getListProductType");
        List<ProductType> productTypes = productTypeService.getAllProductType();
        // List<ProductType> productTypes = productTypeRepo.findAll();
        return ResponseEntity.ok().body(new GetListProductTypeOutputModel(productTypes));

    }

    @PostMapping("/add-new-product-to-db")
    public ResponseEntity<?> addNewProductToDatabase(Principal principal,
                                                    @RequestParam("CreateProductInputModel") String json,
                                                    @RequestParam("files") MultipartFile[] files) {
        log.info("addNewProductToDatabase");
//        log.info("input {}", input.toString());
//        Product product = productService.save(input.getProductId(), input.getProductName(), input.getType(), null, 0,
//                                              input.getQuantityUomId(), null, null, input.getContent());
        UserLogin u = userService.findById(principal.getName());
        Product product = productService.save(u, json, files);
        return ResponseEntity.ok().body(product);
    }


    @GetMapping("/get-list-product-frontend")
    public ResponseEntity<?> getListProductFrontend(Pageable page, @RequestParam(required = false) String param) {
        Page<Product> productPage = productPagingRepo.findAll(page);
        for (Product p : productPage) {
            if (p.getProductType() != null) {
                p.setProductTypeDescription(p.getProductType().getDescription());
            }
            if (p.getUom() != null) {
                p.setUomDescription(p.getUom().getDescription());
            }
        }
        return ResponseEntity.ok().body(productPage);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<?> getProductDetail(@PathVariable String productId) {
//        Product product = productService.findByProductId(productId);
//        ProductDetailModel productDetailModel = new ProductDetailModel(product);
//        log.info(productDetailModel.toString());
        ProductDetailModel productDetailModel = productService.getProductDetail(productId);

        return ResponseEntity.ok().body(productDetailModel);
    }


    @GetMapping("/get-list-product-with-define-page")
    public ResponseEntity<?> getListProductWithDefinePage(Pageable pageable) {
        log.info("page {}", pageable);
        ListProductsByDefinePageModel productPage = productService.getListProductWithPage(pageable);

        return ResponseEntity.ok().body(productPage);
    }

    @GetMapping("/get-product-for-edit/{productId}")
    public ResponseEntity<?> getProductForEdit(@PathVariable String productId) {
        Product product = productService.findByProductId(productId);
        Content primaryImg = product.getPrimaryImg();
        if (primaryImg != null) {
            try {
                Response response = contentService.getContentData(primaryImg.getContentId().toString());
                String base64Flag = "data:image/jpeg;base64," +
                                    Base64.getEncoder().encodeToString(response.body().bytes());
                product.setAvatar(base64Flag);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        return ResponseEntity.ok(new ProductDetailModel(product));
    }

    @GetMapping("/get-list-product-img/{productId}")
    public ResponseEntity<?> getListProductImg(@PathVariable String productId) {
        log.info("getListProductImg");
        Product product = productService.findByProductId(productId);
        UUID primaryImgId = product.getPrimaryImg().getContentId();
        List<ProductImageInfoModel> productImageInfoModels = new ArrayList<ProductImageInfoModel>();

        for (Content content : product.getContents()) {
            if (content != null) {
                try {
                    Response response = contentService.getContentData(content.getContentId().toString());

                    String base64Flag = "data:image/jpeg;base64," +
                                        Base64.getEncoder().encodeToString(response.body().bytes());
                    productImageInfoModels.add(new ProductImageInfoModel(
                        base64Flag,
                        content.getContentId().toString()));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        return ResponseEntity.ok(new ListProductImageModel(productImageInfoModels, primaryImgId.toString()));

    }

    @PostMapping("/set-product-primary-img/{productId}")
    public void setProductPrimaryImg(@PathVariable String productId, @RequestBody PrimaryImgIdModel imput) {
        Product product = productService.findByProductId(productId);
        product.setPrimaryImg(contentRepo.findByContentId(UUID.fromString(imput.getPrimaryImgId())));
        productService.saveProduct(product);
    }

    @PostMapping("/set-product-avatar/{productId}")
    public ResponseEntity<?> setProductAvatar(@PathVariable String productId,
                                              @RequestParam("fileName") String json,
                                              @RequestParam("file") MultipartFile file) {
        ProductDetailModel productDetailModel = productService.saveProductAvatar(productId, json, file);

        return ResponseEntity.ok().body(productDetailModel);
    }

    @PostMapping("/add-new-image/{productId}")
    public void addNewImage(@PathVariable String productId, @RequestBody NewImageModel input) {
        log.info("addNewImage");
        Product product = productService.findByProductId(productId);
        if (product == null) {
            log.info("2222222");
        }
        Set<Content> contents = product.getContents();
        List<String> contentIds = input.getContent();
        if (contentIds.size() > 0) {
            Iterator<Content> contentsIterator = contents.iterator();
            if (contentsIterator != null) {
                while (contentsIterator.hasNext()) {
                    contentIds.add(contentsIterator.next().getContentId().toString());
                }
                log.info("1");
                Set<Content> lC = contentIds
                    .stream()
                    .map(id -> contentRepo.getOne(UUID.fromString(id)))
                    .collect(Collectors.toSet());
                product.setContents(lC);
            }
        }

        Content primaryImg = product.getPrimaryImg();
        if (primaryImg == null && contents.size() > 0) {
            primaryImg = contents.iterator().next();
            product.setPrimaryImg(primaryImg);
        }

        productRepo.save(product);
    }

    @PostMapping("/save-attachment-images/{productId}")
    public ResponseEntity<?> saveAttachmentImages(@PathVariable String productId,
                                                 @RequestParam("fileId") String json,
                                                 @RequestParam("files") MultipartFile[] attachments) {
        ProductDetailModel productDetailModel = productService.saveAttachmentImages(productId, json, attachments);

        return ResponseEntity.ok().body(productDetailModel);
    }
}
