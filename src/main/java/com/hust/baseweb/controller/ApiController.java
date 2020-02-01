package com.hust.baseweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hust.baseweb.entity.SecurityGroup;
import com.hust.baseweb.entity.TrackLocations;
import com.hust.baseweb.entity.UserLogin;
import com.hust.baseweb.model.CreateUserLoginInputModel;
import com.hust.baseweb.model.GetDetailUserLoginInputModel;
import com.hust.baseweb.model.GetDetailUserLoginOutputModel;
import com.hust.baseweb.model.GetUserLoginOutputModel;
import com.hust.baseweb.model.SecurityGroupOutputModel;
import com.hust.baseweb.service.SecurityGroupService;
import com.hust.baseweb.service.UserService;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    
    @GetMapping("/get-list-userlogins")
    public ResponseEntity<List> getListUserLogins(Principal principal){
    	System.out.println(module + "::getListUserLogins");
    	List<UserLogin> lstUserLogin = userService.getAllUserLogins();
    	List<GetUserLoginOutputModel> lst = lstUserLogin.stream().map(e -> new GetUserLoginOutputModel(e.getUserLoginId(), e.getParty().getPartyId()))
    			.collect(Collectors.toList());
    	return ResponseEntity.ok().body(lst);
    }
    
    @PostMapping("/create-userlogin")
    public ResponseEntity createUserLogin(Principal principal, @RequestBody CreateUserLoginInputModel input){
    	System.out.println(module + "::createUserLogin, userName = " + input.getUserName() + ", password = " + input.getPassword());
    	UserLogin userLogin = userService.findById(principal.getName());
    	
    	try{
    		UserLogin createdUserLogin = userService.save(input.getUserName(), input.getPassword());
    		return ResponseEntity.ok().body(createdUserLogin.getUserLoginId());
    	}catch(Exception ex){
    		ex.printStackTrace();
    		String json = "{\"status\":\"EXISTS\"}";
    		return ResponseEntity.status(HttpStatus.CONFLICT).body(json);
    	}    	
    }
    @PostMapping("/get-detail-user-login")
    public ResponseEntity getDetailUserLogin(Principal principal, @RequestBody GetDetailUserLoginInputModel input){
    	System.out.println(module + "::getDetailUserLogin, userName = " + input.getUserName());
    	UserLogin u = userService.findById(input.getUserName());
    	List<SecurityGroup> lst = securityGroupService.findAll();
    	SecurityGroupOutputModel[] allSecurityGroups = new SecurityGroupOutputModel[lst.size()];
    	for(int i = 0; i < lst.size(); i++){
    		SecurityGroup sg = lst.get(i);
    		allSecurityGroups[i] = new SecurityGroupOutputModel(sg.getGroupId(), sg.getDescription());
    	}
    	GetDetailUserLoginOutputModel o = new GetDetailUserLoginOutputModel(u.getUserLoginId(), u.getParty().getPartyId().toString(), 
    			allSecurityGroups);
    	return ResponseEntity.ok().body(o);
    }
}

