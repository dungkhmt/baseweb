package com.hust.baseweb.applications.waterresourcesmanagement.controller;

import com.hust.baseweb.applications.waterresourcesmanagement.entity.Lake;
import com.hust.baseweb.applications.waterresourcesmanagement.model.LakeLiveInfoModel;
import com.hust.baseweb.applications.waterresourcesmanagement.model.LakeModel;
import com.hust.baseweb.applications.waterresourcesmanagement.service.LakeService;
import com.hust.baseweb.entity.UserLogin;
import com.hust.baseweb.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Log4j2
public class LakeAPIController {
    private LakeService lakeService;
    private UserService userService;


    @PostMapping("/create-lake")
    public ResponseEntity<?> createLake(Principal principal, @RequestBody LakeModel lakeModel){
        log.info("createLake, userLoginId = " + principal.getName());
        UserLogin u = userService.findById(principal.getName());
        Lake lake = lakeService.save(u,lakeModel);
        return ResponseEntity.ok().body(lake);
    }

    @GetMapping("/get-lakes-owned-by-userlogin")
    public ResponseEntity<?> getLakesOfOwnedByUserLogin(Principal principal){
        log.info("getLakesOwnedByUserLogin, userLoginId = " + principal.getName());
        UserLogin u = userService.findById(principal.getName()) ;
        List<Lake> lakes = lakeService.getLakesOwnedByUserLogin(u);
        return ResponseEntity.ok().body(lakes);
    }
    @GetMapping("/get-all-lakes")
    public ResponseEntity<?> getAllLakes(Principal principal){
        log.info("getAllLakes, userLoginId = " + principal.getName());
        UserLogin u = userService.findById(principal.getName()) ;
        List<Lake> lakes = lakeService.findAll();
        return ResponseEntity.ok().body(lakes);
    }

    @GetMapping(path = "/lake/{lakeId}")
    public ResponseEntity<?> getLakeDetail(@PathVariable String lakeId, Principal principal){
        log.info("getLakeDetail, lakeId = " + lakeId);
        Lake lake = lakeService.findLakeById(lakeId);
        return ResponseEntity.ok().body(lake);
    }
    @GetMapping(path = "/lakeinfolive/{lakeId}")
    public ResponseEntity<?> getLakeInfoLiveDetail(@PathVariable String lakeId, Principal principal){
        log.info("getLakeInfoLiveDetail, lakeId = " + lakeId);
        //Lake lake = lakeService.getLiveInfoLake(lakeId);
        LakeLiveInfoModel lakeLiveInfoModel = lakeService.getLiveInfoLake(lakeId);
        //return ResponseEntity.ok().body(lake);
        return ResponseEntity.ok().body(lakeLiveInfoModel);
    }
}
