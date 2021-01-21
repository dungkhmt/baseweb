package com.hust.baseweb.applications.postsys.controller;

import com.hust.baseweb.applications.postsys.entity.PostOrder;
import com.hust.baseweb.applications.postsys.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

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
}
