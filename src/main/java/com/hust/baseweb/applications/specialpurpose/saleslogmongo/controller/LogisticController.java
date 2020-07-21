package com.hust.baseweb.applications.specialpurpose.saleslogmongo.controller;

import com.hust.baseweb.applications.specialpurpose.saleslogmongo.model.CreatePurchaseOrderInputModel;
import com.hust.baseweb.applications.specialpurpose.saleslogmongo.model.GetInventoryItemOutputModel;
import com.hust.baseweb.applications.specialpurpose.saleslogmongo.service.LogisticService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@CrossOrigin
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Log4j2
public class LogisticController {

    private final LogisticService logisticService;

    @PostMapping("/mongo/create-purchase-order")
    @ApiOperation(value = "Tạo đơn mua")
    public ResponseEntity<?> createPurchaseOrder(
        Principal principal,
        @ApiParam(value = "Dữ liệu mô tả thông tin đơn tạo")
        @RequestBody CreatePurchaseOrderInputModel input
    ) {
        // TODO
        return null;
    }

    @GetMapping("/mongo/get-inventory-item/{facilityId}")
    @ApiOperation(value = "Danh sách tồn kho")
    public ResponseEntity<GetInventoryItemOutputModel> getInventoryItems(
        Principal principal,
        @ApiParam(value = "Kho cần xem thông tin") @PathVariable String facilityId
    ) {
        return ResponseEntity.ok(logisticService.getInventoryItems(facilityId));
    }
}
