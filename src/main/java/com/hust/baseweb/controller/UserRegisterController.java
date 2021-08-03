package com.hust.baseweb.controller;

import com.hust.baseweb.applications.education.exception.SimpleResponse;
import com.hust.baseweb.model.ApproveRegistrationIM;
import com.hust.baseweb.model.DisableUserRegistrationIM;
import com.hust.baseweb.model.RegisterIM;
import com.hust.baseweb.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
@RestController
@CrossOrigin
public class UserRegisterController {

    private UserService userService;

    public UserRegisterController(UserService userService) {
        this.userService = userService;
    }

    /*@PostMapping("/user/register")
    public ResponseEntity<UserRegister.OutputModel> registerUser(@RequestBody UserRegister.InputModel inputModel) {
        return ResponseEntity.ok(userService.registerUser(inputModel));
    }

    @GetMapping("/user/get-all-register-user")
    public ResponseEntity<List<UserRegister.OutputModel>> findAllRegisterUser() {
        return ResponseEntity.ok(userService.findAllRegisterUser());
    }

    @GetMapping("/user/approve-register/{userLoginId}")
    public ResponseEntity<Boolean> approveRegisterUser(@PathVariable String userLoginId) {
        return ResponseEntity.ok(userService.approveRegisterUser(userLoginId));
    }*/

    @PostMapping("/user/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterIM im) {
        SimpleResponse res = userService.register(im);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @GetMapping("/user/registration-list")
    public ResponseEntity<?> getAllRegists() {
        return ResponseEntity.ok().body(userService.getAllRegists());
    }

    @PostMapping("/user/approve-registration")
    public ResponseEntity<?> approve(@RequestBody ApproveRegistrationIM im) {
        SimpleResponse res = userService.approve(im);
        return ResponseEntity.status(res.getStatus()).body(res);
    }
    @PostMapping("/user/disable-registration")
    public ResponseEntity<?> disableUserRegistration(Principal principal, @RequestBody DisableUserRegistrationIM input){
        SimpleResponse res = userService.disableUserRegistration(input);
        return ResponseEntity.status(res.getStatus()).body(res);
    }
}
