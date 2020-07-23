package com.hust.baseweb.applications.gismap.controller;

import com.hust.baseweb.applications.gismap.document.Street;
import com.hust.baseweb.applications.gismap.model.AddPointStreetInputModel;
import com.hust.baseweb.applications.gismap.model.InitBuildStreetInputModel;
import com.hust.baseweb.applications.gismap.model.StreetInputModel;
import com.hust.baseweb.applications.gismap.model.TerminateBuildStreetInputModel;
import com.hust.baseweb.applications.gismap.service.GisMapService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@CrossOrigin
@Log4j2
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class GisMapController {

    public static ConcurrentHashMap<String, Street> mId2Street = new ConcurrentHashMap<>();

    private GisMapService gisMapService;

    @PostMapping("/gismap/init-build-street")
    public synchronized ResponseEntity<?> initBuildStreet(
        Principal principal,
        @RequestBody InitBuildStreetInputModel input
    ) {
        String streetId = UUID.randomUUID().toString();
        Street street = new Street();
        street.setStreetId(streetId);
        street.setStreetName(input.getStreetName());
        mId2Street.put(streetId, street);
        return ResponseEntity.ok().body(street);
    }

    @PostMapping("/gismap/add-point-street")
    public synchronized ResponseEntity<?> addPointStreet(
        Principal principal,
        @RequestBody AddPointStreetInputModel input
    ) {
        Street street = mId2Street.get(input.getStreetId());
        if (street == null) {
            return ResponseEntity.ok().body("STREET NULL");
        }
        street.addPoint(input.getLat(), input.getLng(), input.getTimestamp());
        return ResponseEntity.ok().body(street);
    }

    @PostMapping("/gismap/terminate-build-street")
    public synchronized ResponseEntity<?> terminateBuildStreet(
        Principal principal, @RequestBody
        TerminateBuildStreetInputModel input
    ) {
        Street street = mId2Street.get(input.getStreetId());
        if (street == null) {
            return ResponseEntity.ok().body("STREET NULL");
        }
        street = gisMapService.save(street);
        mId2Street.remove(street.getStreetId());

        return ResponseEntity.ok().body(street);
    }

    @GetMapping("/gismap/get-all-streets")
    public ResponseEntity<?> getAllStreets(Principal principal){
        return ResponseEntity.ok().body(gisMapService.findAll());
    }

    @GetMapping("/gismap/get-unterminated-streets")
    public ResponseEntity<?> getUnTerminatedStreets(Principal principal){
        List<Street> streetList = new ArrayList<>();
        for(String id: mId2Street.keySet()){
          streetList.add(mId2Street.get(id));
        }
        return ResponseEntity.ok().body(streetList);
    }
    @PostMapping("/gismap/ignore-unterminated-street")
    public synchronized ResponseEntity<?> ignoreUnTerminatedStreet(
        Principal principal, @RequestBody
        StreetInputModel input
    ) {
        Street street = mId2Street.get(input.getStreetId());
        if (street == null) {
            return ResponseEntity.ok().body("STREET NULL");
        }
        mId2Street.remove(street.getStreetId());
        return ResponseEntity.ok().body(street);
    }
    @PostMapping("/gismap/remove-built-street")
    public synchronized ResponseEntity<?> removeBuiltStreet(
        Principal principal, @RequestBody
        StreetInputModel input
    ) {
        Street street = gisMapService.removeStreet(input.getStreetId());
        return ResponseEntity.ok().body(street);
    }
}
