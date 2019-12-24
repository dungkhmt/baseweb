package com.hust.baseweb.service;

import com.hust.baseweb.entity.UserLogin;
import com.hust.baseweb.repo.UserLoginRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements  UserService {
    @Autowired
    private UserLoginRepo userLoginRepo;
    @Override
    public UserLogin findById(String userLoginId) {
        return userLoginRepo.findByUserLoginId(userLoginId);
    }
}
