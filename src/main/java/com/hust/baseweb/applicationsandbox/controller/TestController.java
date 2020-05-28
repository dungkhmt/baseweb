package com.hust.baseweb.applicationsandbox.controller;

import com.hust.baseweb.applicationsandbox.model.AddInputModel;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@Log4j2
public class TestController {

    @PostMapping("/add")
    public ResponseEntity<?> add(Principal principal, @RequestBody AddInputModel input) {
        int c = input.getA() + input.getB();
        log.info("add, a = " + input.getA() + ", b = " + input.getB() + ", c = " + c);
        return ResponseEntity.ok().body(c);
    }
}
