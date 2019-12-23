package com.dailyopt.baseweb.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Controller
public class ApiController {
   @GetMapping("/")
    public ResponseEntity<Map> home(Principal principal){
       Map<String,String> res= new HashMap<>();
       res.put("user",principal.getName());
       return new ResponseEntity<Map>(res, HttpStatus.CREATED);
   }
}

