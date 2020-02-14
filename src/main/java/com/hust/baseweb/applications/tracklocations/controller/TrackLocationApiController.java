package com.hust.baseweb.applications.tracklocations.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Point;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hust.baseweb.applications.geo.cache.GeoPointCache;
import com.hust.baseweb.applications.tracklocations.entity.TrackLocations;
import com.hust.baseweb.applications.tracklocations.model.GetLocationInputModel;
import com.hust.baseweb.applications.tracklocations.model.GetUserLocationsInputModel;
import com.hust.baseweb.applications.tracklocations.model.PostLocationInputModel;
import com.hust.baseweb.applications.tracklocations.model.TrackLocationsOutputModel;
import com.hust.baseweb.applications.tracklocations.model.UserLocationModel;
import com.hust.baseweb.applications.tracklocations.repo.TrackLocationPagingRepo;
import com.hust.baseweb.applications.tracklocations.service.TrackLocationsService;
import com.hust.baseweb.entity.SecurityGroup;
import com.hust.baseweb.entity.UserLogin;
import com.hust.baseweb.model.GetDetailUserLoginInputModel;
import com.hust.baseweb.model.GetDetailUserLoginOutputModel;
import com.hust.baseweb.service.SecurityGroupService;
import com.hust.baseweb.service.UserService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
public class TrackLocationApiController {
	public static final String module = TrackLocationApiController.class.getName();
	
	public static GeoPointCache geoPointCache = new GeoPointCache();
	
	@Autowired
	private UserService userService;
	 
	@Autowired
	private TrackLocationsService trackLocationsService;
	
	@Autowired
	private TrackLocationPagingRepo trackLocationPagingRepo;
	
	 @Autowired
	 SecurityGroupService securityGroupService;
	 
    
    @PostMapping("/post-location")
    public ResponseEntity postLocation(Principal principal, @RequestBody PostLocationInputModel input){
    	
    	UserLogin userLogin=userService.findById(principal.getName());
    	TrackLocations tl = trackLocationsService.save(input, userLogin.getParty());
    	geoPointCache.put(userLogin.getUserLoginId(), input.getLat(), input.getLng());
    	return ResponseEntity.ok().body(tl.getTrackLocationId());
    }
    @PostMapping("/get-location")
    public ResponseEntity getLocation(Principal principal, @RequestBody GetLocationInputModel input){
    	//Point p = trackLocationsService.getLocation(input.getPartyId());
    	Point p = geoPointCache.getLocation(input.getUserLoginId());
    	
    	System.out.println(module + "::getLocation(" + input.getUserLoginId() + "), location = " + (p != null ? p.getX() + "," + p.getY() : "NULL"));
    	if(p == null) return ResponseEntity.ok().body(null);
    	return ResponseEntity.ok().body(p);
    }
    
    @PostMapping("/get-user-locations")
    public ResponseEntity<List> getUSerLocations(Principal principal, @RequestBody GetUserLocationsInputModel input){
    	List<UserLogin> userLogins = userService.getAllUserLogins();
    	List<UserLocationModel> userLocations = new ArrayList<UserLocationModel>();
    	for(UserLogin u: userLogins){
    		Point p = geoPointCache.getLocation(u.getUserLoginId());
    		if(p != null){
    			userLocations.add(new UserLocationModel(u.getUserLoginId(),p.getX(),p.getY()));
    		}
    	}
    	return ResponseEntity.ok().body(userLocations);
    }
    @GetMapping("/get-track-locations")
    public ResponseEntity<List> getTrackLocations(Principal principal){
    	System.out.println("getTrackLocations");
    	
    	UserLogin userLogin=userService.findById(principal.getName());
    	List<TrackLocations> lst = trackLocationsService.getListLocations();
    	List<TrackLocationsOutputModel> ret_lst = lst.stream().map(e -> new TrackLocationsOutputModel(e)).collect(Collectors.toList());
    	
    	return ResponseEntity.ok().body(ret_lst);
    }
    @GetMapping("/tracklocations")
    public ResponseEntity<?> getTracklocations(Pageable page, @RequestParam(required = false) String param){
    	System.out.println(module + "::getTrackLocations, page = pageNumber = " + page.getPageNumber() + ", offSet = " + 
    page.getOffset() + ", pageSize = " + page.getPageSize() + ", param = " + param);
    	
    	Page<TrackLocations> tl = trackLocationPagingRepo.findAll(page);
    	return ResponseEntity.ok().body(tl);
    	
    	
    }
}

