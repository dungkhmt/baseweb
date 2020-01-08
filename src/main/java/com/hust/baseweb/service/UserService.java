package com.hust.baseweb.service;

import java.util.List;

import com.hust.baseweb.entity.UserLogin;

public interface UserService {
    public UserLogin findById(String userLoginId);
    public List<UserLogin> getAllUserLogins();
    public UserLogin save(String userName, String password) throws Exception;
}
