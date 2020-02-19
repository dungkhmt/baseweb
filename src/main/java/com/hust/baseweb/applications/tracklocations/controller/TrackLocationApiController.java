package com.hust.baseweb.applications.tracklocations.controller;

import com.hust.baseweb.applications.geo.cache.GeoPointCache;
import com.hust.baseweb.applications.tracklocations.entity.TrackLocations;
import com.hust.baseweb.applications.tracklocations.model.*;
import com.hust.baseweb.applications.tracklocations.repo.TrackLocationPagingRepo;
import com.hust.baseweb.applications.tracklocations.service.TrackLocationsService;
import com.hust.baseweb.entity.UserLogin;
import com.hust.baseweb.service.SecurityGroupService;
import com.hust.baseweb.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Point;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class TrackLocationApiController {
    public static final String module = TrackLocationApiController.class.getName();

    private static GeoPointCache geoPointCache = new GeoPointCache();

    SecurityGroupService securityGroupService;
    private UserService userService;
    private TrackLocationsService trackLocationsService;
    private TrackLocationPagingRepo trackLocationPagingRepo;

    @PostMapping("/post-location")
    public ResponseEntity postLocation(Principal principal, @RequestBody PostLocationInputModel input) {

        UserLogin userLogin = userService.findById(principal.getName());
        TrackLocations trackLocations = trackLocationsService.save(input, userLogin.getParty());
        geoPointCache.put(userLogin.getUserLoginId(), input.getLat(), input.getLng());
        return ResponseEntity.ok().body(trackLocations.getTrackLocationId());
    }

    @PostMapping("/get-location")
    public ResponseEntity getLocation(Principal principal, @RequestBody GetLocationInputModel input) {
        //Point p = trackLocationsService.getLocation(input.getPartyId());
        Point point = geoPointCache.getLocation(input.getUserLoginId());

        System.out.println(module + "::getLocation(" + input.getUserLoginId() + "), location = " + (point != null ? point.getX() + "," + point.getY() : "NULL"));
        if (point == null) {
            return ResponseEntity.ok().body(null);
        }
        return ResponseEntity.ok().body(point);
    }

    @PostMapping("/get-user-locations")
    public ResponseEntity<List> getUSerLocations(Principal principal, @RequestBody GetUserLocationsInputModel input) {
        List<UserLogin> usersLogin = userService.getAllUserLogins();
        List<UserLocationModel> userLocation = new ArrayList<>();
        for (UserLogin userLogin : usersLogin) {
            Point point = geoPointCache.getLocation(userLogin.getUserLoginId());
            if (point != null) {
                userLocation.add(new UserLocationModel(userLogin.getUserLoginId(), point.getX(), point.getY()));
            }
        }
        return ResponseEntity.ok().body(userLocation);
    }

    @GetMapping("/get-track-locations")
    public ResponseEntity<List> getTrackLocations(Principal principal) {
        System.out.println("getTrackLocations");

        UserLogin userLogin = userService.findById(principal.getName());
        List<TrackLocations> listLocationList = trackLocationsService.getListLocations();
        List<TrackLocationsOutputModel> trackLocationsOutputModels =
                listLocationList.stream().map(TrackLocationsOutputModel::new).collect(Collectors.toList());

        return ResponseEntity.ok().body(trackLocationsOutputModels);
    }

    @GetMapping("/tracklocations")
    public ResponseEntity<?> getTracklocations(Pageable page, @RequestParam(required = false) String param) {
        System.out.println(module + "::getTrackLocations, page = pageNumber = " + page.getPageNumber() + ", offSet = " +
                page.getOffset() + ", pageSize = " + page.getPageSize() + ", param = " + param);

        Page<TrackLocations> trackLocationPage = trackLocationPagingRepo.findAll(page);
        return ResponseEntity.ok().body(trackLocationPage);


    }
}

