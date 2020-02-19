package com.hust.baseweb.applications.logistics.controller;

import com.hust.baseweb.applications.logistics.entity.Facility;
import com.hust.baseweb.applications.logistics.entity.Product;
import com.hust.baseweb.applications.logistics.model.GetListFacilityInputModel;
import com.hust.baseweb.applications.logistics.model.GetListFacilityOutputModel;
import com.hust.baseweb.applications.logistics.model.GetListProductInputModel;
import com.hust.baseweb.applications.logistics.model.GetListProductOutputModel;
import com.hust.baseweb.applications.logistics.service.FacilityService;
import com.hust.baseweb.applications.logistics.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class LogisticsAPIController {
    public static final String module = LogisticsAPIController.class.getName();

    private ProductService productService;
    private FacilityService facilityService;

    @PostMapping("/get-list-facility")
    public ResponseEntity getListFacilities(Principal principal, @RequestBody GetListFacilityInputModel input) {
        // TODO
        List<Facility> facilities = facilityService.getAllFacilities();
        return ResponseEntity.ok().body(new GetListFacilityOutputModel(facilities));
    }

    @PostMapping("/get-list-product")
    public ResponseEntity getListProducts(Principal principal, @RequestBody GetListProductInputModel input) {
        // TODO
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok().body(new GetListProductOutputModel(products));

    }


}
