package com.hust.baseweb.applications.logistics.controller;

import com.hust.baseweb.applications.logistics.entity.Facility;
import com.hust.baseweb.applications.logistics.entity.Product;
import com.hust.baseweb.applications.logistics.entity.ProductPrice;
import com.hust.baseweb.applications.logistics.model.GetListFacilityInputModel;
import com.hust.baseweb.applications.logistics.model.GetListFacilityOutputModel;
import com.hust.baseweb.applications.logistics.model.GetListProductInputModel;
import com.hust.baseweb.applications.logistics.model.GetListProductOutputModel;
import com.hust.baseweb.applications.logistics.model.product.CreateProductInputModel;
import com.hust.baseweb.applications.logistics.model.product.GetProductPriceInputModel;
import com.hust.baseweb.applications.logistics.model.product.SetProductPriceInputModel;
import com.hust.baseweb.applications.logistics.service.FacilityService;
import com.hust.baseweb.applications.logistics.service.ProductPriceService;
import com.hust.baseweb.applications.logistics.service.ProductService;
import com.hust.baseweb.entity.UserLogin;
import com.hust.baseweb.service.UserService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Log4j2
public class LogisticsAPIController {
    public static final String module = LogisticsAPIController.class.getName();

    private ProductService productService;
    private FacilityService facilityService;
    private ProductPriceService productPriceService;
    
    private UserService userService;
    
    @PostMapping("/get-list-facility")
    public ResponseEntity getListFacilities(Principal principal, @RequestBody GetListFacilityInputModel input) {
        // TODO
        List<Facility> facilities = facilityService.getAllFacilities();
        return ResponseEntity.ok().body(new GetListFacilityOutputModel(facilities));
    }

    @PostMapping("/get-list-product")
    public ResponseEntity getListProducts(Principal principal, @RequestBody GetListProductInputModel input) {
    	log.info("getListProducts {}",input.getStatusId());
        // TODO
        List<Product> products = productService.getAllProducts();
        //List<Product> sel_prod = new ArrayList<Product>();
        //for(int i = 0; i < 5; i++)
        //	sel_prod.add(products.get(i));
        return ResponseEntity.ok().body(new GetListProductOutputModel(products));
        //return ResponseEntity.ok().body(new GetListProductOutputModel(sel_prod));
    }

    @PostMapping("/create-product")
    public ResponseEntity<?> createProduct(Principal principal, @RequestBody CreateProductInputModel input){
    	Product product = productService.save(input.getProductId(), input.getProductName(), input.getUomId());
    	return ResponseEntity.ok().body(product);
    }
    
    @PostMapping("/set-product-price")
    public ResponseEntity<?> setProductPrice(Principal principal, @RequestBody SetProductPriceInputModel input){
    	UserLogin userLogin = userService.findById(principal.getName());
    	ProductPrice pp = productPriceService.setProductPrice(userLogin, input.getProductId(), input.getPrice(), input.getCurrencyUomId(), input.getTaxInPrice());
    	return ResponseEntity.ok().body(pp);
    }
    @PostMapping("/get-product-price")
    public ResponseEntity<?> getProductPrice(Principal principal, @RequestBody GetProductPriceInputModel input){
    	ProductPrice pp = productPriceService.getProductPrice(input.getProductId());
    	return ResponseEntity.ok().body(pp);
    }
}
