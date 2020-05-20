package com.hust.baseweb.applications.logistics.controller;

import com.hust.baseweb.applications.logistics.entity.Facility;
import com.hust.baseweb.applications.logistics.entity.InventoryItem;
import com.hust.baseweb.applications.logistics.entity.Product;
import com.hust.baseweb.applications.logistics.model.ExportInventoryItemsInputModel;
import com.hust.baseweb.applications.logistics.model.ImportInventoryItemInputModel;
import com.hust.baseweb.applications.logistics.model.ImportInventoryItemsInputModel;
import com.hust.baseweb.applications.logistics.model.InventoryModel;
import com.hust.baseweb.applications.logistics.repo.FacilityRepo;
import com.hust.baseweb.applications.logistics.repo.ProductRepo;
import com.hust.baseweb.applications.logistics.service.InventoryItemService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController
@CrossOrigin
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class InventoryItemAPIController {
    public static final String module = InventoryItemAPIController.class.getName();

    private InventoryItemService inventoryItemService;
    private FacilityRepo facilityRepo;

    private ProductRepo productRepo;

    @PostMapping("/import-inventory-items")
    @Transactional
    public ResponseEntity<List<InventoryItem>> importInventoryItems(@RequestBody ImportInventoryItemsInputModel inventoryItemsInput) {
//        System.out.println(module + "::importInventoryItems, input.sz = " + input.getInventoryItems().length);

//        for (ImportInventoryItemInputModel inputModel : input.getInventoryItems()) {
//            InventoryItem inventoryItem = inventoryItemService.importInventoryItems(inputModel);
//            if (inventoryItem == null) {
//                // THIS SHOULD BE IMPROVE using transaction
//                return ResponseEntity.unprocessableEntity().body("cannot create inventory item");
//            }
//        }
//        return ResponseEntity.ok().body("ok");

        return ResponseEntity.ok(inventoryItemService.importInventoryItems(inventoryItemsInput));
    }

    @GetMapping("/simulator/import-inventory-items")
    @Transactional
    public ResponseEntity<List<InventoryItem>> simulatorImportInventoryItems() {
        Random random = new Random();
        List<Product> products = productRepo.findAll();
        List<Facility> facilities = facilityRepo.findAll();

        List<ImportInventoryItemInputModel> inventoryItemInputModels = new ArrayList<>();
        for (Facility facility : facilities) {
            for (Product product : products) {
                inventoryItemInputModels.add(new ImportInventoryItemInputModel(
                    product.getProductId(),
                    facility.getFacilityId(),
                    null,
                    random.nextInt(1000) + 100
                ));
            }
        }

        return importInventoryItems(new ImportInventoryItemsInputModel(
            inventoryItemInputModels.toArray(new ImportInventoryItemInputModel[0])));
    }

    @PostMapping("/export-inventory-items")
    public ResponseEntity<?> exportInventoryItems(Principal principal,
                                                  @RequestBody ExportInventoryItemsInputModel input) {
        String response = inventoryItemService.exportInventoryItems(input);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/get-inventory-order-header/page")
    public ResponseEntity<?> getInventoryOrderHeaderPage(Pageable pageable) {
        return ResponseEntity.ok().body(inventoryItemService.getInventoryOrderHeaderPage(pageable));
    }

    @PostMapping("/get-inventory-order-detail")
    public ResponseEntity<?> getInventoryOrderDetail(@RequestBody InventoryModel.OrderFacility orderFacility) {
        return ResponseEntity.ok().body(inventoryItemService.getInventoryOrderHeaderDetail(
            orderFacility.getFacilityId(),
            orderFacility.getOrderId()));
    }

    @GetMapping("/facility/all")
    public ResponseEntity<?> getAllFacility() {
        return ResponseEntity.ok().body(facilityRepo.findAll());
    }

    @GetMapping("/get-inventory-order-export-list/{facilityId}")
    public ResponseEntity<?> getInventoryOrderExportList(@PathVariable String facilityId) {
        return ResponseEntity.ok().body(inventoryItemService.getInventoryExportList(facilityId));
    }

    @GetMapping("/get-inventory-list/{facilityId}")
    public ResponseEntity<?> getInventoryList(@PathVariable String facilityId) {
        return ResponseEntity.ok().body(inventoryItemService.getInventoryList(facilityId));
    }
}
