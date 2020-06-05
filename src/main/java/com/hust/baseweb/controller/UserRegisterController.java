package com.hust.baseweb.controller;

import com.hust.baseweb.entity.UserRegister;
import com.hust.baseweb.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("/user/register")
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
    }
}
