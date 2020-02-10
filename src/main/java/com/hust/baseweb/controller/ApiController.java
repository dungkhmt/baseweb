package com.hust.baseweb.controller;


import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import com.hust.baseweb.service.SecurityGroupService;
import com.hust.baseweb.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class ApiController {
	public static final String module = ApiController.class.getName();
	@Autowired
	private UserService userService;
	 
	//@Autowired
	//private TrackLocationsService trackLocationsService;
	
	 @Autowired
	 SecurityGroupService securityGroupService;
	 
    @GetMapping("/")
    public ResponseEntity<Map> home(Principal principal) {
    	    	
    	System.out.println(module + "::home");
       Map<String, String> res = new HashMap<>();
       res.put("user", principal.getName());
       HttpHeaders responseHeaders = new HttpHeaders();
       responseHeaders.set("Access-Control-Expose-Headers","X-Auth-Token");
       return ResponseEntity.ok().headers(responseHeaders).body(res);
    }
    /*
    @GetMapping("/logout") USE default built by SPRING
    public ResponseEntity<String> logout(Principal principal) {
    	return ResponseEntity.ok().body("");
    }
    */
    
    /*
    @PostMapping("/post-location")
    public ResponseEntity postLocation(Principal principal, @RequestBody PostLocationInputModel input){
    	
    	UserLogin userLogin=userService.findById(principal.getName());
    	TrackLocations tl = trackLocationsService.save(input, userLogin.getParty());
        return ResponseEntity.ok().body(tl.getTrackLocationId());
    }
    @PostMapping("/get-location")
    public ResponseEntity getLocation(Principal principal, @RequestBody GetLocationInputModel input){
    	Point p = trackLocationsService.getLocation(input.getPartyId());
    	if(p == null) return ResponseEntity.ok().body(null);
    	return ResponseEntity.ok().body(p);
    }
    @GetMapping("/get-track-locations")
    public ResponseEntity<List> getTrackLocations(Principal principal){
    	System.out.println("getTrackLocations");
    	UserLogin userLogin=userService.findById(principal.getName());
    	List<TrackLocations> lst = trackLocationsService.getListLocations();
    	List<TrackLocationsOutputModel> ret_lst = lst.stream().map(e -> new TrackLocationsOutputModel(e)).collect(Collectors.toList());
    	
    	return ResponseEntity.ok().body(ret_lst);
    }
    */
    
    
}

