package com.hust.baseweb.applications.postsys.controller;

import com.hust.baseweb.applications.postsys.entity.PostDriverPostOfficeAssignment;
import com.hust.baseweb.applications.postsys.model.postdriver.UpdatePostDriverPostOfficeAssignmentInputModel;
import com.hust.baseweb.applications.postsys.service.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@Log4j2(topic = "POST_LOG")
public class PostmanController {

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

    @GetMapping("/get-order-by-postman-and-date")
    public ResponseEntity getOrderByPostmanAndDate(
        @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy")
            Date fromDate,
        @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") Date toDate,
        Principal principal
    ) {
        return ResponseEntity.ok().body(postmanService.findOrdersByPostmanAndDate(principal, fromDate, toDate));
    }

    @GetMapping("/get-post-office-by-postman")
    public ResponseEntity getPostOfficeByPostman(Principal principal) {
        return ResponseEntity.ok().body(postmanService.getPostOfficeByPostman(principal));
    }

}
