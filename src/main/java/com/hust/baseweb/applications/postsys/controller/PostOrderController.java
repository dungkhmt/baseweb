package com.hust.baseweb.applications.postsys.controller;

import com.hust.baseweb.applications.postsys.entity.PostOrder;
import com.hust.baseweb.applications.postsys.entity.PostPackageType;
import com.hust.baseweb.applications.postsys.entity.PostShipOrderTripPostOfficeAssignment;
import com.hust.baseweb.applications.postsys.model.ResponseSample;
import com.hust.baseweb.applications.postsys.model.postshiporder.CreatePostShipOrderInputModel;
import com.hust.baseweb.applications.postsys.model.postshiporder.CreatePostShipOrderOutputModel;
import com.hust.baseweb.applications.postsys.model.postshiporder.UpdatePostShipOrderInputModel;
import com.hust.baseweb.applications.postsys.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
public class PostOrderController {

    @Autowired
    private PostOfficeService postOfficeService;
    @Autowired
    private PostCustomerService postCustomerService;
    @Autowired
    private PostOrderService postOrderService;
    @Autowired
    PostPackageTypeService postPackageTypeService;
    @Autowired
    PostTripService postTripService;
    @Autowired
    PostmanService postmanService;
    @Autowired
    private PostDriverService postDriverService;

    @PostMapping("/create-post-ship-order")
    public ResponseEntity createPostShipOrder(@RequestBody CreatePostShipOrderInputModel input) {
        CreatePostShipOrderOutputModel result = postOrderService.createPostShipOrder(input);
        return ResponseEntity.ok().body(result);
    }


    @PostMapping("/update-post-order-status")
    public ResponseEntity updatePostOrderStatus(
        @RequestBody UpdatePostShipOrderInputModel updatePostShipOrderInputModel
    ) {
        return ResponseEntity.ok().body(postOrderService.updatePostOrderStatus(updatePostShipOrderInputModel));
    }

    @DeleteMapping("/delete-post-ship-order/{postShipOrderID}")
    public ResponseEntity deletePostShipOrder(@PathVariable(required = true) String postShipOrderID) {
        ResponseSample response = postOrderService.CancelPostOrder(postShipOrderID);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/get-post-package-type")
    public ResponseEntity getPostPackageType() {
        List<PostPackageType> result = postPackageTypeService.getListPostPackageType();
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/get_office_order_detail/{postOfficeId}")
    public ResponseEntity getOfficeOrderDetail(
        @PathVariable String postOfficeId,
        @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") Date fromDate,
        @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") Date toDate
    ) {
        return ResponseEntity.ok().body(postOfficeService.getOfficeOrderDetailOutput(postOfficeId, fromDate, toDate));
    }

    @GetMapping("/get-list-order")
    public ResponseEntity getListOrder(
        @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") Date fromDate,
        @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") Date toDate
    ) {
        List<PostOrder> result = postOrderService.findAllPostOrder(fromDate, toDate);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/get_post_order_route")
    public ResponseEntity getPostOrderRoute(
        @RequestParam String postOrderId
    ) {
        return ResponseEntity.ok().body(postOrderService.getPostOrderRoute(postOrderId));
    }

    @GetMapping("/get-order-by-day/{day}")
    public ResponseEntity getPostOrderByDay(
        @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date day
    ) {
        return ResponseEntity.ok().body(postOrderService.findAllOrderByDay(day));
    }

    /**
     * Lấy thông tin đơn hàng đã được gán cho postman thành công
     *
     * @param postOfficeId
     * @param fromDate
     * @param toDate
     * @return
     */
    @GetMapping("/get-postman-list-order-bydate/{postOfficeId}")
    public ResponseEntity getPostmanListAndOrderList(
        @PathVariable String postOfficeId,
        @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy")
            Date fromDate,
        @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") Date toDate,
        @RequestParam boolean from
    ) {
        return ResponseEntity
            .ok()
            .body(postmanService.findOrdersByPostOfficeIdAndDate(postOfficeId, fromDate, toDate, from));
    }

    @GetMapping("/get-order-by-trip")
    public ResponseEntity getPostOrderByTrip(
        @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy")
            Date fromDate,
        @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") Date toDate
    ) {
        List<PostShipOrderTripPostOfficeAssignment> postShipOrderTripPostOfficeAssignments = postTripService.getPostOrderByTrip(
            fromDate,
            toDate);
        return ResponseEntity.ok().body(postShipOrderTripPostOfficeAssignments);
    }
}
