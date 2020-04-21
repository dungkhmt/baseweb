package com.hust.baseweb.applications.logistics.controller;

import java.security.Principal;
import java.util.List;

import com.hust.baseweb.applications.logistics.entity.Product;
import com.hust.baseweb.applications.logistics.entity.ProductType;
import com.hust.baseweb.applications.logistics.entity.Uom;
import com.hust.baseweb.applications.logistics.model.GetListProductTypeOutputModel;
import com.hust.baseweb.applications.logistics.model.GetListUomOutputModel;
import com.hust.baseweb.applications.logistics.model.InputModel;
import com.hust.baseweb.applications.logistics.model.ModelCreateProductInput;
import com.hust.baseweb.applications.logistics.model.product.ProductDetailModel;
import com.hust.baseweb.applications.logistics.repo.ProductPagingRepo;
import com.hust.baseweb.applications.logistics.repo.ProductTypeRepo;
import com.hust.baseweb.applications.logistics.service.ProductService;
import com.hust.baseweb.applications.logistics.service.ProductTypeService;
import com.hust.baseweb.applications.logistics.service.UomService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.log4j.Log4j2;

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
    ProductController(UomService uomService, ProductTypeService productTypeService, ProductService productService,
            ProductTypeRepo productTypeRepo, ProductPagingRepo productPagingRepo) {
        this.uomService = uomService;
        this.productTypeService = productTypeService;
        this.productService = productService;
        this.productTypeRepo = productTypeRepo;
        this.productPagingRepo = productPagingRepo;
    }

    @PostMapping("/get-list-uoms")
    public ResponseEntity getListUoms(Principal principal, @RequestBody InputModel input) {
        log.info("getListUoms {}", input.getStatusId());
        List<Uom> uoms = uomService.getAllUoms();
        log.info("uoms size: {}", uoms.size());

        return ResponseEntity.ok().body(new GetListUomOutputModel(uoms));

    }

    @PostMapping("/get-list-product-type")
    public ResponseEntity getListProductType(Principal principal, @RequestBody InputModel input) {
        log.info("getListProductType");
        List<ProductType> productTypes = productTypeService.getAllProductType();
        // List<ProductType> productTypes = productTypeRepo.findAll();
        return ResponseEntity.ok().body(new GetListProductTypeOutputModel(productTypes));

    }

    @PostMapping("/add-new-product-to-db")
    public ResponseEntity addNewProductToDatabase(Principal principal, @RequestBody ModelCreateProductInput input) {
        log.info("addNewProductToDatabase");
        log.info("input {}", input.toString());
        Product product = productService.save(input.getProductId(), input.getProductName(), input.getType(), null, 0,
                input.getQuantityUomId(), null, null, input.getContent());
        return ResponseEntity.status(HttpStatus.CREATED).body(product.getProductId());
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
        Product product = productService.findByProductId(productId);
        ProductDetailModel productDetailModel = new ProductDetailModel(product);
        return ResponseEntity.ok().body(productDetailModel);
    }
}
