package com.hust.baseweb.applications.postsys.controller;

import com.hust.baseweb.applications.postsys.entity.*;
import com.hust.baseweb.applications.postsys.model.ResponseSample;
import com.hust.baseweb.applications.postsys.model.postcustomer.CreatePostCustomerModel;
import com.hust.baseweb.applications.postsys.model.postoffice.CreatePostOfficeInputModel;
import com.hust.baseweb.applications.postsys.model.postshiporder.CreatePostShipOrderInputModel;
import com.hust.baseweb.applications.postsys.model.postshiporder.CreatePostShipOrderOutputModel;
import com.hust.baseweb.applications.postsys.model.posttrip.CreatePostTripModel;
import com.hust.baseweb.applications.postsys.service.*;
import com.poiji.bind.Poiji;
import com.poiji.exception.PoijiExcelType;
import com.poiji.option.PoijiOptions;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
@Log4j2(topic = "POST_LOG")
public class PostAPIController {

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
    @PostMapping("/create-post-office")
    public ResponseEntity<?> createPostOffice(Principal principal, @RequestBody CreatePostOfficeInputModel input) {
        PostOffice newPostOffice = postOfficeService.save(input);
        log.info("createPostOffice, new post office name = " + newPostOffice.getPostOfficeName());
        return ResponseEntity.ok().body(newPostOffice);
    }

    @GetMapping("/get-all-post-office")
    public ResponseEntity<?> getAllPostOffice(Principal principal) {
        List<PostOffice> result = postOfficeService.findAll();
        log.info("getAllPostOffice, " + result.size() + " item(s) sent.");
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/upload-post-office-list")
    public ResponseEntity<?> uploadPostOfficeList(@RequestParam("file") MultipartFile multipartFile
    ) throws IOException {
        List<CreatePostOfficeInputModel> createPostOfficeInputModels
            = Poiji.fromExcel(multipartFile.getInputStream(), PoijiExcelType.XLSX, CreatePostOfficeInputModel.class,
                              PoijiOptions.PoijiOptionsBuilder.settings().sheetName("DanhSachBuuCuc").build());
        List<PostOffice> postOffices = postOfficeService.save(createPostOfficeInputModels);
        log.info("Uploaded " + postOffices.size() + " postoffice");
        return ResponseEntity.ok().body(postOffices.size());
    }

    @GetMapping("/get-post-office-by-id/{postOfficeId}")
    public ResponseEntity<?> getPostOfficeById(Principal principal, @PathVariable String postOfficeId) {
        PostOffice result = postOfficeService.findByPostOfficeId(postOfficeId);
        log.info("getPostOfficeById = " + postOfficeId);
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/delete-post-office/{postOfficeId}")
    public ResponseEntity<?> deletePostOfficeById(Principal principal, @PathVariable String postOfficeId) {
        postOfficeService.deleteByPostOfficeId(postOfficeId);
        log.info("deletePostOfficeById = " + postOfficeId);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/get-customer-list")
    public ResponseEntity<?> getCustomerList() {
        List<PostCustomer> result = postCustomerService.findAll();
        log.info("getAllPostOffice, " + result.size() + " item(s) sent.");
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/register-customer")
    public ResponseEntity saveCustomer(@RequestBody CreatePostCustomerModel createPostCustomerModel) {
        PostCustomer result = postCustomerService.saveCustomer(createPostCustomerModel);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/get-list-order")
    public ResponseEntity getListOrder() {
        List<PostOrder> result = postOrderService.findAllPostOrder();
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/create-post-ship-order")
    public ResponseEntity createPostShipOrder(@RequestBody CreatePostShipOrderInputModel input) {
        CreatePostShipOrderOutputModel result = postOrderService.createPostShipOrder(input);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/find-customer-by-partyid")
    public ResponseEntity findCustomerByPartyId(Principal principal) {
        PostCustomer result = postCustomerService.findCustomerByPartyId(principal);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/get-post-package-type")
    public ResponseEntity getPostPackageType() {
        List<PostPackageType> result = postPackageTypeService.getListPostPackageType();
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/delete-post-ship-order/{postShipOrderID}")
    public ResponseEntity deletePostShipOrder(@PathVariable(required = true) String postShipOrderID ) {
        ResponseSample response = postOrderService.CancelPostOrder(postShipOrderID);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/get-post-trip-list")
    public ResponseEntity getVehicleList() {
        List<PostTrip> postTrips = postTripService.findAllVehicle();
        return ResponseEntity.ok().body(postTrips);
    }

    @GetMapping("/get-post-trip/{postTripId}")
    public ResponseEntity getVehicle(@PathVariable(required = true) String postTripId) {
        PostTrip postTrip = postTripService.findByPostOfficeFixedTripId(postTripId);
        return ResponseEntity.ok().body(postTrip);
    }

    @GetMapping("/create-post-trip")
    public ResponseEntity createVehicle(@RequestBody CreatePostTripModel creatPostTripModel) {
        PostTrip postTrip = postTripService.createPostTrip(creatPostTripModel);
        return ResponseEntity.ok().body(postTrip);
    }

    @GetMapping("/create-post-trip-list")
    public ResponseEntity createVehicleList(@RequestParam("file") MultipartFile multipartFile) throws IOException  {
        List<CreatePostOfficeInputModel> createPostOfficeInputModels
            = Poiji.fromExcel(multipartFile.getInputStream(), PoijiExcelType.XLSX, CreatePostOfficeInputModel.class,
                              PoijiOptions.PoijiOptionsBuilder.settings().sheetName("DanhSachBuuCuc").build());
        List<PostOffice> postOffices = postOfficeService.save(createPostOfficeInputModels);
        log.info("Uploaded " + postOffices.size() + " postoffice");
        return ResponseEntity.ok().body(postOffices.size());
    }
}
