package com.hust.baseweb.applications.tmscontainer.controller;

import com.hust.baseweb.applications.geo.entity.GeoPoint;
import com.hust.baseweb.applications.geo.entity.PostalAddress;
import com.hust.baseweb.applications.geo.repo.GeoPointRepo;
import com.hust.baseweb.applications.geo.repo.PostalAddressRepo;
import com.hust.baseweb.applications.tms.entity.ContDepotContainer;
import com.hust.baseweb.applications.tms.model.InputDepotContainerModel;
import com.hust.baseweb.applications.tms.model.OutputContDepotContainerModel;
import com.hust.baseweb.applications.tmscontainer.entity.ContPort;
import com.hust.baseweb.applications.tmscontainer.model.InputContPortModel;
import com.hust.baseweb.applications.tmscontainer.model.OutputContPortModel;
import com.hust.baseweb.applications.tmscontainer.repo.ContPortPagingRepo;
import com.hust.baseweb.applications.tmscontainer.repo.ContPortRepo;
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
public class TMSContainerAPIController {
    private ContPortPagingRepo contPortPagingRepo;
    private ContPortRepo contPortRepo;
    private PostalAddressRepo postalAddressRepo;
    private GeoPointRepo geoPointRepo;



    @PostMapping("/create-port")
    public void createPort(@RequestBody InputContPortModel input){
        ContPort contPort = new ContPort();
        contPort.setPortId(input.getPortId());
        contPort.setPortName(input.getPortName());
        GeoPoint geoPoint = new GeoPoint();
        geoPoint.setLatitude(Double.parseDouble(input.getLat()));
        geoPoint.setLongitude(Double.parseDouble(input.getLng()));
        PostalAddress postalAddress = new PostalAddress();
        postalAddress.setAddress(input.getAddress());
        postalAddress.setGeoPoint(geoPoint);
        contPort.setPostalAddress(postalAddress);
        geoPointRepo.save(geoPoint);
        postalAddressRepo.save(postalAddress);
        contPortRepo.save(contPort);
    }


    @GetMapping("/get-list-cont-port-page")
    public ResponseEntity<?> getListContPortPage(Pageable pageable){
        Page<ContPort> contDepotContainerPage = contPortPagingRepo.findAll(pageable);
        for (ContPort contPort: contDepotContainerPage) {
            contPort.setAddress(contPort.getPostalAddress().getAddress());
        }
        return ResponseEntity.ok(contDepotContainerPage);
    }

    @GetMapping("/get-list-cont-port")
    public ResponseEntity<?> getListContPort(){
        List<ContPort> contPorts = contPortRepo.findAll();
        for (ContPort contDepotContainer: contPorts) {
            contDepotContainer.setLat(contDepotContainer.getPostalAddress().getGeoPoint().getLatitude());
            contDepotContainer.setLng(contDepotContainer.getPostalAddress().getGeoPoint().getLongitude());
        }
        return ResponseEntity.ok(new OutputContPortModel(contPorts));
    }

}
