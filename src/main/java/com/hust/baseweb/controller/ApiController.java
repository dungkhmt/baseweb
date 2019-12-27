package com.hust.baseweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hust.baseweb.entity.TrackLocations;
import com.hust.baseweb.entity.UserLogin;
import com.hust.baseweb.model.PostLocationInputModel;
import com.hust.baseweb.model.TrackLocationsOutputModel;
import com.hust.baseweb.service.TrackLocationsService;
import com.hust.baseweb.service.UserService;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
public class ApiController {
	@Autowired
	private UserService userService;
	 
	@Autowired
	private TrackLocationsService trackLocationsService;
	
    @GetMapping("/")
    public ResponseEntity<Map> home(Principal principal) {
        Map<String, String> res = new HashMap<>();
        res.put("user", principal.getName());
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Access-Control-Expose-Headers","X-Auth-Token");
        return ResponseEntity.ok().headers(responseHeaders).body(res);
    }
    
    @PostMapping("/post-location")
    public ResponseEntity postLocation(Principal principal, @RequestBody PostLocationInputModel input){
    	
    	UserLogin userLogin=userService.findById(principal.getName());
    	TrackLocations tl = trackLocationsService.save(input, userLogin.getParty());
    	return ResponseEntity.ok().body(tl.getTrackLocationId());
    }
    
    @GetMapping("/get-track-locations")
    public ResponseEntity<List> getTrackLocations(Principal principal){
    	System.out.println("getTrackLocations");
    	UserLogin userLogin=userService.findById(principal.getName());
    	List<TrackLocations> lst = trackLocationsService.getListLocations();
    	List<TrackLocationsOutputModel> ret_lst = lst.stream().map(e -> new TrackLocationsOutputModel(e)).collect(Collectors.toList());
    	
    	return ResponseEntity.ok().body(ret_lst);
    }
    
}

