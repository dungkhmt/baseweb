package com.hust.baseweb.applications.logistics.controller;


import com.hust.baseweb.applications.logistics.entity.Product;
import com.hust.baseweb.applications.logistics.entity.ProductType;
import com.hust.baseweb.applications.logistics.entity.Uom;
import com.hust.baseweb.applications.logistics.model.*;
import com.hust.baseweb.applications.logistics.repo.ProductPagingRepo;
import com.hust.baseweb.applications.logistics.repo.ProductTypeRepo;
import com.hust.baseweb.applications.logistics.service.ProductService;
import com.hust.baseweb.applications.logistics.service.ProductTypeService;
import com.hust.baseweb.applications.logistics.service.UomService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Log4j2
public class ProductController {

    private UomService uomService;
    private ProductTypeService productTypeService;
    private ProductService productService;
    private ProductTypeRepo productTypeRepo;
    private ProductPagingRepo productPagingRepo;


    @PostMapping("/get-list-uoms")
    public ResponseEntity getListUoms(Principal principal, @RequestBody InputModel input){
        log.info("getListUoms {}",input.getStatusId());
        List<Uom> uoms = uomService.getAllUoms();
        log.info("uoms size: {}",uoms.size());

        return ResponseEntity.ok().body(new GetListUomOutputModel(uoms));

    }


    @PostMapping("/get-list-product-type")
    public ResponseEntity getListProductType(Principal principal, @RequestBody InputModel input){
        log.info("getListProductType");
        List<ProductType> productTypes = productTypeService.getAllProductType();
        //List<ProductType> productTypes = productTypeRepo.findAll();
        return ResponseEntity.ok().body(new GetListProductTypeOutputModel(productTypes));

    }

    @PostMapping("/add-new-product-to-db")
    public ResponseEntity addNewProductToDatabase(Principal principal, @RequestBody ModelCreateProductInput input){
        log.info("addNewProductToDatabase");
        log.info("input {}",input.toString());
        Product product = new Product();
        product.setProductId(input.getProductId());
        product.setProductName(input.getProductName());
        product.setProductType(productTypeService.getProductTypeByProductTypeId(input.getType()));
        product.setUom(uomService.getUomByUomId(input.getQuantityUomId()));
        productService.saveProduct(product);
        return ResponseEntity.ok().body(new Product());
    }

    @GetMapping("/get-list-product-frontend")
    public ResponseEntity<?> getListProductFrontend(Pageable page, @RequestParam(required = false) String param){
        Page<Product> productPage = productPagingRepo.findAll(page);
        for (Product p: productPage ) {
            if(p.getProductType() != null)
            	p.setProductTypeDescription(p.getProductType().getDescription());
            if(p.getUom() != null)
            	p.setUomDescription(p.getUom().getDescription());
        }
        return ResponseEntity.ok().body(productPage);
    }
}
