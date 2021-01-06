package com.hust.baseweb.applications.postsys.controller;

import com.hust.baseweb.applications.postsys.entity.*;
import com.hust.baseweb.applications.postsys.model.ResponseSample;
import com.hust.baseweb.applications.postsys.model.postcustomer.CreatePostCustomerModel;
import com.hust.baseweb.applications.postsys.model.postdriver.PostDriverUpdateInputModel;
import com.hust.baseweb.applications.postsys.model.postdriver.UpdatePostDriverPostOfficeAssignmentInputModel;
import com.hust.baseweb.applications.postsys.model.postman.PostmanAssignInput;
import com.hust.baseweb.applications.postsys.model.postman.PostmanUpdateInputModel;
import com.hust.baseweb.applications.postsys.model.postoffice.CreatePostOfficeInputModel;
import com.hust.baseweb.applications.postsys.model.postshiporder.CreatePostShipOrderInputModel;
import com.hust.baseweb.applications.postsys.model.postshiporder.CreatePostShipOrderOutputModel;
import com.hust.baseweb.applications.postsys.model.postshiporder.UpdatePostShipOrderInputModel;
import com.hust.baseweb.applications.postsys.model.posttrip.CreatePostTripModel;
import com.hust.baseweb.applications.postsys.model.posttrip.ExecuteTripInputModel;
import com.hust.baseweb.applications.postsys.service.*;
import com.poiji.bind.Poiji;
import com.poiji.exception.PoijiExcelType;
import com.poiji.option.PoijiOptions;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
    @Autowired
    PostmanService postmanService;
    @Autowired
    private PostDriverService postDriverService;

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
    public ResponseEntity<?> uploadPostOfficeList(
        @RequestParam("file") MultipartFile multipartFile
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


    @PostMapping("/update-post-order-status")
    public ResponseEntity updatePostOrderStatus(
        @RequestBody UpdatePostShipOrderInputModel updatePostShipOrderInputModel
    ) {
        return ResponseEntity.ok().body(postOrderService.updatePostOrderStatus(updatePostShipOrderInputModel));
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
    public ResponseEntity deletePostShipOrder(@PathVariable(required = true) String postShipOrderID) {
        ResponseSample response = postOrderService.CancelPostOrder(postShipOrderID);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/get-post-trip-list")
    public ResponseEntity getPostTripList() {
        List<PostFixedTrip> postFixedTrips = postTripService.findAllVehicle();
        return ResponseEntity.ok().body(postFixedTrips);
    }

    @GetMapping("/get-post-execute-trip-list")
    public ResponseEntity getPostTripExecuteList(
        @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy")
            Date date
    ) {
        return ResponseEntity.ok().body(postTripService.getExecuteTripByDate(date));
    }

    @GetMapping("/get-post-trip-list-by-postman")
    public ResponseEntity getPostTripListByPostman(Principal principal) {
        List<PostFixedTrip> postFixedTrips = postTripService.findAllVehicleByPostman(principal);
        return ResponseEntity.ok().body(postFixedTrips);
    }

    @GetMapping("/get-post-execute-trip-list-by-postman")
    public ResponseEntity getPostTripExecuteListByPostman(
        @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy")
            Date date,
        Principal principal
    ) {
        return ResponseEntity.ok().body(postTripService.getExecuteTripByDateAndPostman(date, principal));
    }

    @GetMapping("/get-post-trip-list-by-post-driver")
    public ResponseEntity getPostTripListByPostDriver(Principal principal) {
        List<PostFixedTrip> postFixedTrips = postTripService.findAllTripByPostDriver(principal);
        return ResponseEntity.ok().body(postFixedTrips);
    }

    @GetMapping("/get-post-trip-list-by-post-driver/{postDriverId}")
    public ResponseEntity getPostTripListByPostDriver(@PathVariable String postDriverId) {
        List<PostFixedTrip> postFixedTrips = postTripService.findAllTripByPostDriver(UUID.fromString(postDriverId));
        return ResponseEntity.ok().body(postFixedTrips);
    }

    @GetMapping("/get-post-execute-trip-list-by-post-driver")
    public ResponseEntity getPostTripExecuteListByDriver(
        @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy")
            Date date,
        Principal principal
    ) {
        return ResponseEntity.ok().body(postTripService.getExecuteTripByDateAndPostDriver(date, principal));
    }

    @GetMapping("/get-post-trip/{postTripId}")
    public ResponseEntity getPostTrip(@PathVariable String postTripId) {
        PostFixedTrip postFixedTrip = postTripService.findByPostOfficeFixedTripId(postTripId);
        return ResponseEntity.ok().body(postFixedTrip);
    }

    @PostMapping("/create-post-trip")
    public ResponseEntity createPostTrip(@RequestBody CreatePostTripModel creatPostTripModel) {
        PostFixedTrip postFixedTrip = postTripService.createPostTrip(creatPostTripModel);
        return ResponseEntity.ok().body(postFixedTrip);
    }


    @PostMapping("/create-post-trip-list")
    public ResponseEntity createPostTripList(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        List<CreatePostOfficeInputModel> createPostOfficeInputModels
            = Poiji.fromExcel(multipartFile.getInputStream(), PoijiExcelType.XLSX, CreatePostOfficeInputModel.class,
                              PoijiOptions.PoijiOptionsBuilder.settings().sheetName("DanhSachBuuCuc").build());
        List<PostOffice> postOffices = postOfficeService.save(createPostOfficeInputModels);
        log.info("Uploaded " + postOffices.size() + " postoffice");
        return ResponseEntity.ok().body(postOffices.size());
    }

    @DeleteMapping("/delete-post-trip/{postTripId}")
    public ResponseEntity<?> deletePostTripById(@PathVariable String postTripId) {
        postTripService.deleteByPostTripId(postTripId);
        log.info("deletePostOfficeById = " + postTripId);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/get_office_order_detail/{postOfficeId}")
    public ResponseEntity getOfficeOrderDetail(
        @PathVariable String postOfficeId, @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy")
        Date fromDate,
        @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") Date toDate
    ) {
        return ResponseEntity.ok().body(postOfficeService.getOfficeOrderDetailOutput(postOfficeId, fromDate, toDate));
    }

    @GetMapping("/get-postman-list")
    public ResponseEntity getPostmanList() {
        return ResponseEntity.ok().body(postmanService.findAll());
    }

    @GetMapping("/get-postman-list/{postOfficeId}")
    public ResponseEntity getPostmanList(@PathVariable String postOfficeId) {
        return ResponseEntity.ok().body(postmanService.findByPostOfficeId(postOfficeId));
    }

    @PostMapping("/update-postman")
    public ResponseEntity updatePostman(@RequestBody PostmanUpdateInputModel postmanUpdateInputModel) {
        return ResponseEntity.ok().body(postmanService.updatePostman(postmanUpdateInputModel));
    }

    @GetMapping("/get-post-driver-list")
    public ResponseEntity getPostDriverList() {
        return ResponseEntity.ok().body(postDriverService.findAll());
    }

    @PostMapping("/update-post-driver")
    public ResponseEntity updatePostDriver(@RequestBody PostDriverUpdateInputModel postDriverUpdateInputModel) {
        return ResponseEntity.ok().body(postDriverService.updatePostDriver(postDriverUpdateInputModel));
    }

    @PostMapping("/update-post-driver-post-office-assignment")
    public ResponseEntity updatePostDriverPostOfficeAssignment(
        @RequestBody
            UpdatePostDriverPostOfficeAssignmentInputModel updatePostDriverPostOfficeAssignmentInputModel
    ) {
        return ResponseEntity
            .ok()
            .body(postDriverService.updatePostDriverPostOfficeAssignment(updatePostDriverPostOfficeAssignmentInputModel));
    }

    @PostMapping("/execute-trip")
    public ResponseEntity executeTrip(@RequestBody ExecuteTripInputModel executeTripInputModel) {
        return ResponseEntity.ok().body(postTripService.createPostTripExecute(executeTripInputModel));
    }

    @PostMapping("/update-execute-trip")
    public ResponseEntity updateExecuteTrip(@RequestBody ExecuteTripInputModel executeTripInputModel) {
        return ResponseEntity.ok().body(postTripService.updatePostTripExecute(executeTripInputModel));
    }

    @GetMapping("/get-order-by-day/{day}")
    public ResponseEntity getPostOrderByDay(
        @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date day
    ) {
        return ResponseEntity.ok().body(postOrderService.findAllOrderByDay(day));
    }

    @GetMapping("/get-postman-list-order/{postOfficeId}")
    public ResponseEntity getPostmanListAndOrderList(@PathVariable String postOfficeId) {
        return ResponseEntity.ok().body(postmanService.findOrdersByPostOfficeId(postOfficeId));
    }

    @GetMapping("/get-postman-list-order-bydate/{postOfficeId}")
    public ResponseEntity getPostmanListAndOrderList(
        @PathVariable String postOfficeId,
        @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy")
            Date fromDate,
        @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") Date toDate
    ) {
        return ResponseEntity.ok().body(postmanService.findOrdersByPostOfficeIdAndDate(postOfficeId, fromDate, toDate));
    }

    @PostMapping("/submit-postman-assign")
    public ResponseEntity getPostmanListAndOrderList(@RequestBody List<PostmanAssignInput> postmanAssignInputs) {
        return ResponseEntity.ok().body(postmanService.createAssignment(postmanAssignInputs));
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
