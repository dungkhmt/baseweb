package com.hust.baseweb.applications.logistics.controller;

import com.hust.baseweb.applications.logistics.entity.Facility;
import com.hust.baseweb.applications.logistics.entity.Product;
import com.hust.baseweb.applications.logistics.entity.ProductPrice;
import com.hust.baseweb.applications.logistics.entity.Supplier;
import com.hust.baseweb.applications.logistics.model.*;
import com.hust.baseweb.applications.logistics.model.product.GetProductPriceInputModel;
import com.hust.baseweb.applications.logistics.model.product.SetProductPriceInputModel;
import com.hust.baseweb.applications.logistics.service.FacilityService;
import com.hust.baseweb.applications.logistics.service.ProductPriceService;
import com.hust.baseweb.applications.logistics.service.ProductService;
import com.hust.baseweb.applications.logistics.service.SupplierService;
import com.hust.baseweb.entity.UserLogin;
import com.hust.baseweb.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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
    private SupplierService supplierService;

    private UserService userService;

    @PostMapping("/create-facility")
    public ResponseEntity<?> createFacility(@RequestBody FacilityModel facilityModel) {
        log.info("::createFacility(), facilityId=" + facilityModel.getFacilityId());
        return ResponseEntity.ok(facilityService.save(facilityModel));
    }

    @PostMapping("/create-facilities")
    public ResponseEntity<?> createFacility(@RequestBody List<FacilityModel> facilityModels) {
        return ResponseEntity.ok(facilityService.saveAll(facilityModels));
    }

    @PostMapping("/get-list-facility")
    public ResponseEntity<GetListFacilityOutputModel> getListFacilities(Principal principal,
                                                                        @RequestBody GetListFacilityInputModel input) {
        // TODO
        List<Facility> facilities = facilityService.getAllFacilities();
        return ResponseEntity.ok().body(new GetListFacilityOutputModel(facilities));
    }

    @PostMapping("/get-list-product")
    public ResponseEntity<GetListProductOutputModel> getListProducts(Principal principal,
                                                                     @RequestBody GetListProductInputModel input) {
        log.info("getListProducts...");
        // TODO
        List<Product> products = productService.getAllProducts();
        //List<Product> sel_prod = new ArrayList<Product>();
        //for(int i = 0; i < 5; i++)
        //	sel_prod.add(products.get(i));
        return ResponseEntity.ok().body(new GetListProductOutputModel(products));
        //return ResponseEntity.ok().body(new GetListProductOutputModel(sel_prod));
    }

//    @PostMapping("/create-product")
//    public ResponseEntity<?> createProduct(Principal principal, @RequestBody CreateProductInputModel input) {
//        Product product = productService.save(input.getProductId(), input.getProductName(), input.getWeight(), input.getUomId());
//        return ResponseEntity.ok().body(product);
//    }

    @PostMapping("/set-product-price")
    public ResponseEntity<?> setProductPrice(Principal principal, @RequestBody SetProductPriceInputModel input) {
        UserLogin userLogin = userService.findById(principal.getName());
        ProductPrice productPrice = productPriceService.setProductPrice(userLogin,
                input.getProductId(),
                input.getPrice(),
                input.getCurrencyUomId(),
                input.getTaxInPrice(),
                input.getFromDate(),
                input.getThruDate());
        return ResponseEntity.ok().body(productPrice);
    }

    @PostMapping("/get-product-price")
    public ResponseEntity<?> getProductPrice(Principal principal, @RequestBody GetProductPriceInputModel input) {
        ProductPrice pp = productPriceService.getProductPrice(input.getProductId());
        return ResponseEntity.ok().body(pp);
    }

    @GetMapping("/get-product-price-history/{productId}")
    public ResponseEntity<List<ProductPrice.Model>> getProductPriceHistory(@PathVariable String productId) {
        return ResponseEntity.ok(productPriceService.getProductPriceHistory(productId));
    }

    //@PostMapping("/get-sale-reports")
    //public ResponseEntity<?> getSaleReports(@RequestBody SaleReportModel.Input input) {
    //   return ResponseEntity.ok().body(productPriceService.getSaleReports(input));
    //}

    @GetMapping("/get-all-supplier")
    public ResponseEntity<List<Supplier>> getAllSupplier() {
        return ResponseEntity.ok(supplierService.getAllSupplier());
    }

    @PostMapping("/create-supplier")
    public ResponseEntity<Supplier> createSupplier(@RequestBody Supplier.CreateModel supplierModel) {
        return ResponseEntity.ok(supplierService.create(supplierModel));
    }
}
