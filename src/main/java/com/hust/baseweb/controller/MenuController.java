package com.hust.baseweb.controller;

import com.hust.baseweb.constant.ApplicationTypeConstant;
import com.hust.baseweb.entity.SecurityGroup;
import com.hust.baseweb.entity.SecurityPermission;
import com.hust.baseweb.entity.UserLogin;
import com.hust.baseweb.service.ApplicationService;
import com.hust.baseweb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
public class MenuController {
	public static final String module = MenuController.class.getName();
    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private UserService userService;
    @GetMapping("/menu")
    public ResponseEntity<Set> getMenu(Principal principal){
        UserLogin userLogin=userService.findById(principal.getName());
        System.out.println(module + "::getMenu, userName = " + principal.getName());
        
        List<SecurityPermission> permissionList= new ArrayList<>();
        for(SecurityGroup sg:userLogin.getRoles()) 
        	permissionList.addAll(sg.getPermissions());
        System.out.println(module + "::getMenu, userName = " + principal.getName() + ", meu.lst = " + permissionList.size());
        return ResponseEntity.ok().body(applicationService.getListByPermissionAndType(permissionList,ApplicationTypeConstant.MENU).stream().map(ap->ap.getApplicationId()).collect(Collectors.toSet()));
    }
}
