package com.hust.baseweb.applications.geo.controller;

import com.hust.baseweb.applications.geo.embeddable.DistanceTravelTimePostalAddressEmbeddableId;
import com.hust.baseweb.applications.geo.entity.DistanceTravelTimePostalAddress;
import com.hust.baseweb.applications.geo.entity.Enumeration;
import com.hust.baseweb.applications.geo.entity.GeoPoint;
import com.hust.baseweb.applications.geo.entity.PostalAddress;
import com.hust.baseweb.applications.geo.model.ComputeMissingDistanceInputModel;
import com.hust.baseweb.applications.geo.model.InputModelDistanceTravelTimePostalAddress;
import com.hust.baseweb.applications.geo.model.InputModelGetInfoPostalAddressChangeWithGoogleMap;
import com.hust.baseweb.applications.geo.model.ListEnumerationOutputModel;
import com.hust.baseweb.applications.geo.repo.*;
import com.hust.baseweb.applications.geo.service.DistanceTravelTimePostalAddressService;
import com.hust.baseweb.applications.logistics.model.InputModel;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Log4j2
public class GeoAPIController {

    private PostalAddressPagingRepo postalAddressPagingRepo;
    private PostalAddressRepo postalAddressRepo;
    private GeoPointRepo geoPointRepo;
    private DistanceTravelTimePostalAddressPagingRepo distanceTravelTimePostalAddressPagingRepo;
    private DistanceTravelTimePostalAddressRepo distanceTravelTimePostalAddressRepo;
    private EnumerationRepo enumerationRepo;
    private DistanceTravelTimePostalAddressService distanceTravelTimePostalAddressService;

    @PostMapping("/compute-missing-address-distances")
    public ResponseEntity<?> computeMissingAddressDistance(
        Principal principal,
        @RequestBody ComputeMissingDistanceInputModel input) {

        int cnt = distanceTravelTimePostalAddressService.computeMissingDistance(
            input.getDistanceSource(),
            input.getSpeedTruck(),
            input.getSpeedMotorbike(),
            input.getMaxElements());
        return ResponseEntity.ok().body(cnt);
    }

    @GetMapping("/get-list-geo-point-page")
    ResponseEntity<?> getListGeoPoint(Pageable page, @RequestParam(required = false) String param) {

        log.info("getListGeoPoint");
        Page<PostalAddress> postalAddressPage = postalAddressPagingRepo.findAll(page);
        for (PostalAddress postalAddress : postalAddressPage) {
            Double latitude = postalAddress.getGeoPoint().getLatitude();
            Double longitude = postalAddress.getGeoPoint().getLongitude();
            String coordinates = "" + latitude + ", " + longitude;
            postalAddress.setCoordinates(coordinates);
        }
        return ResponseEntity.ok().body(postalAddressPage);

    }

    @PostMapping("/get-info-postal-to-display-in-map/{contactMechId}")
    public ResponseEntity<?> getInfoPostalToDisplayInMap(
        @PathVariable String contactMechId,
        @RequestBody InputModel inputModel) {

        log.info("getInfoPostalToDisplayInMap");
        PostalAddress postalAddress = postalAddressRepo.findByContactMechId(UUID.fromString(contactMechId));
        Double lat = postalAddress.getGeoPoint().getLatitude();
        Double lng = postalAddress.getGeoPoint().getLongitude();
        String coordinates = "" + lat + ", " + lng;
        postalAddress.setCoordinates(coordinates);
        postalAddress.setLng(lng);
        postalAddress.setLat(lat);
        return ResponseEntity.ok().body(postalAddress);
    }

    @PostMapping("/geo-change-location-info-with-googlemap/{contactMechId}")
    // TODO: fix typo --> google-map in frontend
    public void geoChangeLocationInfoWithGoogleMap(
        @PathVariable String contactMechId,
        @RequestBody InputModelGetInfoPostalAddressChangeWithGoogleMap input) {

        PostalAddress postalAddress = postalAddressRepo.findByContactMechId(UUID.fromString(contactMechId));
        postalAddress.setAddress(input.getAddress());
        GeoPoint geoPoint = postalAddress.getGeoPoint();
        geoPoint.setLatitude(Double.parseDouble(input.getLat()));
        geoPoint.setLongitude(Double.parseDouble(input.getLng()));
        geoPointRepo.save(geoPoint);
        postalAddressRepo.save(postalAddress);
    }

    @GetMapping("/get-list-distance-info")
    public ResponseEntity<?> getListDistanceInfo(Pageable page, @RequestParam(required = false) String param) {

        log.info("getListDistanceInfo");
        Page<DistanceTravelTimePostalAddress> distanceTravelTimePostalAddressPage =
            distanceTravelTimePostalAddressPagingRepo.findAll(page);


        for (DistanceTravelTimePostalAddress d : distanceTravelTimePostalAddressPage) {
            UUID idStart = d.getDistanceTravelTimePostalAddressEmbeddableId().getFromContactMechId();
            UUID idEnd = d.getDistanceTravelTimePostalAddressEmbeddableId().getToContactMechId();
            d.setIdStart(idStart);
            d.setIdEnd(idEnd);
            PostalAddress postalAddressStart = postalAddressRepo.findByContactMechId(idStart);
            PostalAddress postalAddressEnd = postalAddressRepo.findByContactMechId(idEnd);
            d.setAddressStart(postalAddressStart.getAddress());
            d.setAddressEnd(postalAddressEnd.getAddress());
            d.setEnumID(d.getEnumeration().getEnumId());
        }
        return ResponseEntity.ok().body(distanceTravelTimePostalAddressPage);
    }

    @PostMapping("/get-distance-postal-address-info-with-key/{fromContactMechId}/{toContactMechId}")
    ResponseEntity<?> getDistancePostalAddressInfoWithKey(
        @PathVariable String fromContactMechId,
        @PathVariable String toContactMechId,
        @RequestBody InputModel inputModel) {

        log.info("getDistancePostalAddressInfoWithKey");
        DistanceTravelTimePostalAddressEmbeddableId distanceTravelTimePostalAddressEmbeddableId =
            new DistanceTravelTimePostalAddressEmbeddableId(
                UUID.fromString(fromContactMechId),
                UUID.fromString(toContactMechId));
        DistanceTravelTimePostalAddress distanceTravelTimePostalAddress =
            distanceTravelTimePostalAddressRepo.findByDistanceTravelTimePostalAddressEmbeddableId(
                distanceTravelTimePostalAddressEmbeddableId);
        distanceTravelTimePostalAddress.setEnumID(distanceTravelTimePostalAddress.getEnumeration().getEnumId());
        return ResponseEntity.ok().body(distanceTravelTimePostalAddress);
    }

    @PostMapping("/get-list-enumeration-distance-source")
    ResponseEntity<?> getListEnumerationDistanceSource(@RequestBody InputModel inputModel) {

        log.info("getListEnumeration");
        //List<Enumeration> enumerationList = enumerationRepo.findAll();
        List<Enumeration> enumerationList = enumerationRepo.findByEnumTypeId("DISTANCE_SOURCE");
        return ResponseEntity.ok().body(new ListEnumerationOutputModel(enumerationList));
    }

    @PostMapping("/get-list-enumeration")
    ResponseEntity<?> getListEnumeration(@RequestBody InputModel inputModel) {

        log.info("getListEnumeration");
        List<Enumeration> enumerationList = enumerationRepo.findAll();
        return ResponseEntity.ok().body(new ListEnumerationOutputModel(enumerationList));
    }

    @PostMapping("/change-distance-travel-time-postal-address-info")
    public void changeDistanceTravelTimePostalAddressInfo(@RequestBody InputModelDistanceTravelTimePostalAddress data) {

        DistanceTravelTimePostalAddressEmbeddableId distanceTravelTimePostalAddressEmbeddableId =
            new DistanceTravelTimePostalAddressEmbeddableId(
                UUID.fromString(data.getFromContactMechId()),
                UUID.fromString(data.getToContactMechId())
            );
        DistanceTravelTimePostalAddress distanceTravelTimePostalAddress
            = distanceTravelTimePostalAddressRepo.findByDistanceTravelTimePostalAddressEmbeddableId(
            distanceTravelTimePostalAddressEmbeddableId);
        distanceTravelTimePostalAddress.setDistance((int) data.getDistance());
        distanceTravelTimePostalAddress.setTravelTime((int) data.getTravelTime());
        distanceTravelTimePostalAddress.setTravelTimeMotorbike((int) data.getTravelTimeMotobike());
        distanceTravelTimePostalAddress.setTravelTimeTruck((int) data.getTravelTimeTruck());
        distanceTravelTimePostalAddress.setEnumeration(enumerationRepo.findByEnumId(data.getEnumId()));
        distanceTravelTimePostalAddressRepo.save(distanceTravelTimePostalAddress);
    }


}
