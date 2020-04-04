package com.hust.baseweb.applications.geo.controller;

import com.hust.baseweb.applications.geo.embeddable.DistanceTraveltimePostalAddressEmbeddableId;
import com.hust.baseweb.applications.geo.entity.DistanceTraveltimePostalAddress;
import com.hust.baseweb.applications.geo.entity.Enumeration;
import com.hust.baseweb.applications.geo.entity.GeoPoint;
import com.hust.baseweb.applications.geo.entity.PostalAddress;
import com.hust.baseweb.applications.geo.model.InputModelDistanceTraveltimePostalAddress;
import com.hust.baseweb.applications.geo.model.InputModelGetInfoPostalAddressChangeWithGoogleMap;
import com.hust.baseweb.applications.geo.model.ListEnumerationOutputModel;
import com.hust.baseweb.applications.geo.repo.*;
import com.hust.baseweb.applications.logistics.model.InputModel;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    private DistanceTraveltimePostalAddressPagingRepo distanceTraveltimePostalAddressPagingRepo;
    private DistanceTraveltimePostalAddressRepo distanceTraveltimePostalAddressRepo;
    private EnumerationRepo enumerationRepo;



    @GetMapping("/get-list-geo-point-page")
    ResponseEntity<?> getListGeoPoint(Pageable page,@RequestParam(required = false) String param){
        log.info("getListGeoPoint");
        Page<PostalAddress> postalAddressPage = postalAddressPagingRepo.findAll(page);
        for(PostalAddress postalAddress: postalAddressPage){
            String latitude = postalAddress.getGeoPoint().getLatitude();
            String longitude = postalAddress.getGeoPoint().getLongitude();
            String cooridinates = "" + latitude + ", " + longitude;
            postalAddress.setCoordinates(cooridinates);
        }
        return ResponseEntity.ok().body(postalAddressPage);

    }

    @PostMapping("/get-info-postal-to-display-in-map/{contactMechId}")
    ResponseEntity<?> getInfoPostalToDisplatInMap(@PathVariable String contactMechId, @RequestBody InputModel inputModel){
        log.info("getInfoPostalToDisplatInMap");
        PostalAddress postalAddress = postalAddressRepo.findByContactMechId(UUID.fromString(contactMechId));
        String lat = postalAddress.getGeoPoint().getLatitude();
        String lng = postalAddress.getGeoPoint().getLongitude();
        String coordinates = "" + lat +", " +lng;
        postalAddress.setCoordinates(coordinates);
        postalAddress.setLng(lng);
        postalAddress.setLat(lat);
        return ResponseEntity.ok().body(postalAddress);
    }

    @PostMapping("/geo-change-location-info-with-googlemap/{contactMechId}")
    void geoChangeLacationInfoWithGooglemap(@PathVariable String contactMechId,
                                                         @RequestBody InputModelGetInfoPostalAddressChangeWithGoogleMap input){
        PostalAddress postalAddress = postalAddressRepo.findByContactMechId(UUID.fromString(contactMechId));
        postalAddress.setAddress(input.getAddress());
        GeoPoint geoPoint = postalAddress.getGeoPoint();
        geoPoint.setLatitude(input.getLat());
        geoPoint.setLongitude(input.getLng());
        geoPointRepo.save(geoPoint);
        postalAddressRepo.save(postalAddress);
    }

    @GetMapping("/get-list-distance-info")
    ResponseEntity<?> getListDistanceInfo(Pageable page,@RequestParam(required = false) String param){
        log.info("getListDistanceInfo");
        Page<DistanceTraveltimePostalAddress> distanceTraveltimePostalAddressPage =
                distanceTraveltimePostalAddressPagingRepo.findAll(page);



        for(DistanceTraveltimePostalAddress d : distanceTraveltimePostalAddressPage){
            UUID idStart = d.getDistanceTraveltimePostalAddressEmbeddableId().getFromContactMechId();
            UUID idEnd = d.getDistanceTraveltimePostalAddressEmbeddableId().getToContactMechId();
            d.setIdStart(idStart);
            d.setIdEnd(idEnd);
            PostalAddress postalAddressStart = postalAddressRepo.findByContactMechId(idStart);
            PostalAddress postalAddressEnd = postalAddressRepo.findByContactMechId(idEnd);
            d.setAddressStart(postalAddressStart.getAddress());
            d.setAddressEnd(postalAddressEnd.getAddress());
            d.setEnumID(d.getEnumeration().getEnumId());
        }
        return ResponseEntity.ok().body(distanceTraveltimePostalAddressPage);
    }

    @PostMapping("/get-distance-postal-address-info-with-key/{fromContactMechId}/{toContactMechId}")
    ResponseEntity<?> getDistancePostalAddressInfoWithKey(@PathVariable String fromContactMechId, @PathVariable String toContactMechId,
                                                          @RequestBody InputModel inputModel){
        log.info("getDistancePostalAddressInfoWithKey");
        DistanceTraveltimePostalAddressEmbeddableId distanceTraveltimePostalAddressEmbeddableId =
                new DistanceTraveltimePostalAddressEmbeddableId(UUID.fromString(fromContactMechId), UUID.fromString(toContactMechId));
        DistanceTraveltimePostalAddress distanceTraveltimePostalAddress =
                distanceTraveltimePostalAddressRepo.findByDistanceTraveltimePostalAddressEmbeddableId(distanceTraveltimePostalAddressEmbeddableId);
        distanceTraveltimePostalAddress.setEnumID(distanceTraveltimePostalAddress.getEnumeration().getEnumId());
        return ResponseEntity.ok().body(distanceTraveltimePostalAddress);
    }

    @PostMapping("/get-list-enumeration")
    ResponseEntity<?> getListEnumeration(@RequestBody InputModel inputModel){
        log.info("getListEnumeration");
        List<Enumeration> enumerationList = enumerationRepo.findAll();
        return ResponseEntity.ok().body(new ListEnumerationOutputModel(enumerationList));
    }

    @PostMapping("/change-distance-travel-time-postal-address-info")
    void changeDistanceTravelTimePostalAddressInfo(@RequestBody InputModelDistanceTraveltimePostalAddress data){
        DistanceTraveltimePostalAddressEmbeddableId distanceTraveltimePostalAddressEmbeddableId =
                new DistanceTraveltimePostalAddressEmbeddableId(
                        UUID.fromString(data.getFromContactMechId()),
                        UUID.fromString(data.getToContactMechId())
                );
        DistanceTraveltimePostalAddress distanceTraveltimePostalAddress
                = distanceTraveltimePostalAddressRepo.findByDistanceTraveltimePostalAddressEmbeddableId(distanceTraveltimePostalAddressEmbeddableId);
        distanceTraveltimePostalAddress.setDistance(data.getDistance());
        distanceTraveltimePostalAddress.setTravelTime(data.getTravelTime());
        distanceTraveltimePostalAddress.setTravelTimeMotobike(data.getTravelTimeMotobike());
        distanceTraveltimePostalAddress.setTravelTimeTruck(data.getTravelTimeTruck());
        distanceTraveltimePostalAddress.setEnumeration(enumerationRepo.findByEnumId(data.getEnumId()));
        distanceTraveltimePostalAddressRepo.save(distanceTraveltimePostalAddress);
    }




}