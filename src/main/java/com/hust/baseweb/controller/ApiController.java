package com.hust.baseweb.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
public class ApiController {
    @GetMapping("/")
    public ResponseEntity<Map> home(Principal principal) {
        Map<String, String> res = new HashMap<>();
        res.put("user", principal.getName());
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Access-Control-Expose-Headers","X-Auth-Token");
        return ResponseEntity.ok().headers(responseHeaders).body(res);
    }
}

