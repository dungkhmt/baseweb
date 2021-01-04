package com.hust.baseweb.controller;


import com.hust.baseweb.entity.*;
import com.hust.baseweb.model.PasswordChangeModel;
import com.hust.baseweb.service.ApplicationService;
import com.hust.baseweb.service.PersonService;
import com.hust.baseweb.service.SecurityGroupService;
import com.hust.baseweb.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ApiController {

    private UserService userService;

    private PersonService personService;

    private ApplicationService applicationService;

    private SecurityGroupService securityGroupService;

    @GetMapping("/")
    public ResponseEntity<Map> home(Principal principal) {
        UserLogin userLogin = userService.findById(principal.getName());
        Map<String, String> response = new HashMap<>();

        response.put("user", principal.getName());
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Access-Control-Expose-Headers", "X-Auth-Token");
        return ResponseEntity.ok().headers(responseHeaders).body(response);
    }

    @GetMapping("/check-authority")
    public ResponseEntity<?> checkAuthorities(Principal principal, @RequestParam String applicationId) {

        Map<String, String> response = null;
        UserLogin userLogin = userService.findById(principal.getName());
        Application application = applicationService.getById(applicationId);
        if (application == null) {

            response = new HashMap<>();
            response.put("status", "SUCESSS");
            response.put("result", "NOT_FOUND");

            return ResponseEntity.ok().body(response);
        }
        List<SecurityPermission> permissionList = new ArrayList<>();
        for (SecurityGroup securityGroup : userLogin.getRoles()) {
            permissionList.addAll(securityGroup.getPermissions());
        }
        Set<String> permissionSet = permissionList.stream().map(permission -> permission.getPermissionId())
                                                  .collect(Collectors.toSet());
        if (permissionSet.contains(application.getPermission().getPermissionId())) {

            response = new HashMap<>();
            response.put("status", "SUCESSS");
            response.put("result", "INCLUDED");
        } else {
            response = new HashMap<>();
            response.put("status", "SUCESSS");
            response.put("result", "NOT_INCLUDED");
        }
        return ResponseEntity.ok().body(response);

    }

    @GetMapping("/my-account")
    public ResponseEntity<?> getAccount(Principal principal) {
        UserLogin userLogin = userService.findById(principal.getName());
        Party party = userLogin.getParty();
        Person person = personService.findByPartyId(party.getPartyId());
        Map<String, String> response = new HashMap<>();
        response.put("name", person.getFullName());
        response.put("partyId", person.getPartyId().toString());
        response.put("user", principal.getName());
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(Principal principal, @RequestBody PasswordChangeModel passwordChangeModel) {
        UserLogin userLogin = userService.findById(principal.getName());
        if (UserLogin.PASSWORD_ENCODER.matches(passwordChangeModel.getCurrentPassword(), userLogin.getPassword())) {
            UserLogin user = userService.updatePassword(userLogin, passwordChangeModel.getNewPassword());
            return ResponseEntity.ok().body("");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password isn't correct");
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

    @GetMapping("/roles")
    public ResponseEntity<?> getRoles() {
        return ResponseEntity.ok().body(securityGroupService.getRoles());
    }

    @GetMapping("screen-security")
    public ResponseEntity<?> getScrSecurInfo(Principal principal) {
        return ResponseEntity.ok().body(applicationService.getScrSecurInfo(principal.getName()));
    }

    /*Jedis jedis = new Jedis("localhost");
        Map<String, String> res = jedis.hgetAll("spring:session:sessions:154894ef-efe4-4acf-b479-bce0275694fd");
        return ResponseEntity.ok().body(res.get("sessionAttr:SPRING_SECURITY_CONTEXT"));*/
}

