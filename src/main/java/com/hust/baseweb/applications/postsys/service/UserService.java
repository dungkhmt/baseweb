package com.hust.baseweb.applications.postsys.service;

import com.hust.baseweb.applications.postsys.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    public String getSecurityGroup(Principal principal) {
        return userRepo.findSecurityGroupByUser(principal.getName());
    }
}
