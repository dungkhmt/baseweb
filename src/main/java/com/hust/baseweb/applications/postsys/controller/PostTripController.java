package com.hust.baseweb.applications.postsys.controller;

import com.hust.baseweb.applications.postsys.entity.PostDriverPostOfficeAssignment;
import com.hust.baseweb.applications.postsys.entity.PostFixedTrip;
import com.hust.baseweb.applications.postsys.entity.PostOffice;
import com.hust.baseweb.applications.postsys.model.postdriver.UpdatePostDriverPostOfficeAssignmentInputModel;
import com.hust.baseweb.applications.postsys.model.postoffice.CreatePostOfficeInputModel;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@Log4j2(topic = "POST_LOG")
public class PostTripController {

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


    @DeleteMapping("/delete-post-driver-post-office-assignment")
    public ResponseEntity deletePostDriverPostOfficeAssignment(
        @RequestBody
            UpdatePostDriverPostOfficeAssignmentInputModel updatePostDriverPostOfficeAssignmentInputModel
    ) {
        return ResponseEntity
            .ok()
            .body(postDriverService.deletePostDriverPostOfficeAssignment(updatePostDriverPostOfficeAssignmentInputModel));
    }

    @GetMapping("/get-office-assignment-list-by-post-driver/{postDriverId}")
    public ResponseEntity getPostTripListByPostDriver(@PathVariable String postDriverId) {
        List<PostDriverPostOfficeAssignment> postDriverPostOfficeAssignments = postTripService.findAllPostOfficeAssignmentByPostDriver(
            UUID.fromString(postDriverId));
        return ResponseEntity.ok().body(postDriverPostOfficeAssignments);
    }

    @PostMapping(("/add-post-driver-post-office-assignment"))
    public ResponseEntity addPostDriverPostOfficeAssignment(
        @RequestBody
            UpdatePostDriverPostOfficeAssignmentInputModel updatePostDriverPostOfficeAssignmentInputModel
    ) {
        return ResponseEntity
            .ok()
            .body(postDriverService.addPostDriverPostOfficeAssignment(updatePostDriverPostOfficeAssignmentInputModel));
    }

    @DeleteMapping("/delete-execute-trip")
    public ResponseEntity deletePostTripExecute(@RequestBody ExecuteTripInputModel executeTripInputModel) {
        return ResponseEntity.ok().body(postTripService.deletePostTripExecute(executeTripInputModel));
    }

    @PostMapping("/execute-trip")
    public ResponseEntity executeTrip(@RequestBody ExecuteTripInputModel executeTripInputModel) {
        return ResponseEntity.ok().body(postTripService.createPostTripExecute(executeTripInputModel));
    }

    @PostMapping("/update-execute-trip")
    public ResponseEntity updateExecuteTrip(@RequestBody ExecuteTripInputModel executeTripInputModel) {
        return ResponseEntity.ok().body(postTripService.updatePostTripExecute(executeTripInputModel));
    }
}
