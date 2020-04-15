package com.hust.baseweb.applications.tms.controller;

import com.hust.baseweb.applications.geo.entity.GeoPoint;
import com.hust.baseweb.applications.geo.entity.PostalAddress;
import com.hust.baseweb.applications.geo.repo.GeoPointRepo;
import com.hust.baseweb.applications.geo.repo.PostalAddressRepo;
import com.hust.baseweb.applications.tms.entity.*;
import com.hust.baseweb.applications.tms.model.*;
import com.hust.baseweb.applications.tms.repo.*;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Log4j2
public class ContainerAPIController {
    private ContContainerTypeRepo contContainerTypeRepo;
    private ContContainerRepo contContainerRepo;
    private ContDepotContainerRepo contDepotContainerRepo;
    private PostalAddressRepo postalAddressRepo;
    private GeoPointRepo geoPointRepo;
    private ContDepotContainerPagingRepo contDepotContainerPagingRepo;
    private ContContainerPagingRepo contContainerPagingRepo;
    private ContDepotTrailerRepo contDepotTrailerRepo;
    private ContDepotTrailerPagingRepo contDepotTrailerPagingRepo;
    private ContDepotTruckRepo contDepotTruckRepo;
    private ContDepotTruckPagingRepo contDepotTruckPagingRepo;
    private ContTrailerPagingRepo contTrailerPagingRepo;
    private ContTrailerRepo contTrailerRepo;

    @GetMapping("/get-list-cont-container")
    public ResponseEntity<?> getListContContainer(Pageable pageable){
        log.info("getListContContainerPage");
        Page<ContContainer> contContainerPage = contContainerPagingRepo.findAll(pageable);
        log.info("size {}",contContainerPage.getSize());
        for (ContContainer contContainer: contContainerPage){
            contContainer.setContainerType(contContainer.getContContainerType().getDescription());
        }
        return ResponseEntity.ok(contContainerPage);

    }

    @GetMapping("/get-list-container-type")
    public ResponseEntity getListContainerType(){
        log.info("getListContainerType");
        List<ContContainerType> contContainerTypes = contContainerTypeRepo.findAll();
        return ResponseEntity.ok().body(new OutputContContainerTypeModel(contContainerTypes));
    }

    @PostMapping("/save-container-to-db")
    public void saveContainer(@RequestBody InputContainerModel input){
        log.info("saveContainer");
        ContContainer contContainer = new ContContainer();
        contContainer.setContainerId(input.getContainerId());
        contContainer.setContainerName(input.getContainerName());
        contContainer.setContContainerType(contContainerTypeRepo.findByContainerTypeId(input.getContainerType()));
        contContainerRepo.save(contContainer);
    }




    @PostMapping("/create-depot-container")
    public void createDepotContainer(@RequestBody InputDepotContainerModel input){
        log.info("createDepotContainer");
        ContDepotContainer contDepotContainer = new ContDepotContainer();
        contDepotContainer.setDepotContainerId(input.getDepotContainerId());
        contDepotContainer.setDepotContainerName(input.getDepotContainerName());
        GeoPoint geoPoint = new GeoPoint();
        geoPoint.setLatitude(Double.parseDouble(input.getLat()));
        geoPoint.setLongitude(Double.parseDouble(input.getLng()));
        PostalAddress postalAddress = new PostalAddress();
        postalAddress.setAddress(input.getAddress());
        postalAddress.setGeoPoint(geoPoint);
        contDepotContainer.setPostalAddress(postalAddress);
        geoPointRepo.save(geoPoint);
        postalAddressRepo.save(postalAddress);
        contDepotContainerRepo.save(contDepotContainer);
    }

    @GetMapping("/get-list-depot-container-page")
    public ResponseEntity<?> getListDepotContainerPage(Pageable pageable){
        Page<ContDepotContainer> contDepotContainerPage = contDepotContainerPagingRepo.findAll(pageable);
        for (ContDepotContainer contDepotContainer: contDepotContainerPage) {
            contDepotContainer.setAddress(contDepotContainer.getPostalAddress().getAddress());
        }
        return ResponseEntity.ok(contDepotContainerPage);
    }

    @GetMapping("/get-list-depot-container")
    public ResponseEntity<?> getListDepotContainer(){
        List<ContDepotContainer> depotContainers = contDepotContainerRepo.findAll();
        for (ContDepotContainer contDepotContainer: depotContainers) {
            contDepotContainer.setLat(contDepotContainer.getPostalAddress().getGeoPoint().getLatitude());
            contDepotContainer.setLng(contDepotContainer.getPostalAddress().getGeoPoint().getLongitude());
        }
        return ResponseEntity.ok(new OutputContDepotContainerModel(depotContainers));
    }


    @PostMapping("/create-depot-trailer")
    public void createDepotTrailer(@RequestBody InputDepotTrailerModel input){
        ContDepotTrailer contDepotTrailer = new ContDepotTrailer();
        contDepotTrailer.setDepotTrailerId(input.getDepotTrailerId());
        contDepotTrailer.setDepotTrailerName(input.getDepotTrailerName());
        GeoPoint geoPoint = new GeoPoint();
        geoPoint.setLatitude(Double.parseDouble(input.getLat()));
        geoPoint.setLongitude(Double.parseDouble(input.getLng()));
        PostalAddress postalAddress = new PostalAddress();
        postalAddress.setAddress(input.getAddress());
        postalAddress.setGeoPoint(geoPoint);
        contDepotTrailer.setPostalAddress(postalAddress);
        geoPointRepo.save(geoPoint);
        postalAddressRepo.save(postalAddress);
        contDepotTrailerRepo.save(contDepotTrailer);
    }


    @GetMapping("/get-list-depot-trailer-page")
    public ResponseEntity<?> getListDepotTrailerPage(Pageable pageable){
        Page<ContDepotTrailer> contDepotTrailerPage = contDepotTrailerPagingRepo.findAll(pageable);
        for (ContDepotTrailer contDepotTrailer: contDepotTrailerPage) {
            contDepotTrailer.setAddress(contDepotTrailer.getPostalAddress().getAddress());
        }
        return ResponseEntity.ok(contDepotTrailerPage);
    }

    @GetMapping("/get-list-depot-trailer")
    public ResponseEntity<?> getListDepotTrailer(){
        List<ContDepotTrailer> depotTrailers = contDepotTrailerRepo.findAll();
        for (ContDepotTrailer contDepotTrailer: depotTrailers) {
            contDepotTrailer.setLat(contDepotTrailer.getPostalAddress().getGeoPoint().getLatitude());
            contDepotTrailer.setLng(contDepotTrailer.getPostalAddress().getGeoPoint().getLongitude());
        }
        return ResponseEntity.ok(new OutputContDepotTrailerModel(depotTrailers));
    }




    @PostMapping("/create-depot-truck")
    public void createDepotTruck(@RequestBody InputDepotTruckModel input){
        ContDepotTruck contDepotTruck = new ContDepotTruck();
        contDepotTruck.setDepotTruckId(input.getDepotTruckId());
        contDepotTruck.setDepotTruckName(input.getDepotTruckName());
        GeoPoint geoPoint = new GeoPoint();
        geoPoint.setLatitude(Double.parseDouble(input.getLat()));
        geoPoint.setLongitude(Double.parseDouble(input.getLng()));
        PostalAddress postalAddress = new PostalAddress();
        postalAddress.setAddress(input.getAddress());
        postalAddress.setGeoPoint(geoPoint);
        contDepotTruck.setPostalAddress(postalAddress);
        geoPointRepo.save(geoPoint);
        postalAddressRepo.save(postalAddress);
        contDepotTruckRepo.save(contDepotTruck);
    }

    @GetMapping("/get-list-depot-truck-page")
    public ResponseEntity<?> getListDepotTruckPage(Pageable pageable){
        Page<ContDepotTruck> contDepotTruckPage = contDepotTruckPagingRepo.findAll(pageable);
        for (ContDepotTruck contDepotTruck: contDepotTruckPage) {
            contDepotTruck.setAddress(contDepotTruck.getPostalAddress().getAddress());
        }
        return ResponseEntity.ok(contDepotTruckPage);
    }

    @GetMapping("/get-list-depot-truck")
    public ResponseEntity<?> getListDepotTruck(){
        List<ContDepotTruck> contDepotTrucks = contDepotTruckRepo.findAll();
        for (ContDepotTruck contDepotTruck: contDepotTrucks) {
            contDepotTruck.setLat(contDepotTruck.getPostalAddress().getGeoPoint().getLatitude());
            contDepotTruck.setLng(contDepotTruck.getPostalAddress().getGeoPoint().getLongitude());
        }
        return ResponseEntity.ok(new OutputContDepotTruckModel(contDepotTrucks));
    }


    @GetMapping("/get-list-cont-trailer")
    public ResponseEntity<?> getListContTrailer(Pageable pageable){
        Page<ContTrailer> contTrailers = contTrailerPagingRepo.findAll(pageable);

        return ResponseEntity.ok(contTrailers);

    }

    @PostMapping("/save-trailer-to-db")
    void saveTrailer(@RequestBody InputTrailerModel input){
        ContTrailer contTrailer = new ContTrailer();
        contTrailer.setTrailerId(input.getTrailerId());
        contTrailer.setDescription(input.getDescription());
        contTrailerRepo.save(contTrailer);
    }




}
