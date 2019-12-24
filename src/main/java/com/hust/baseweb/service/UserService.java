package com.hust.baseweb.service;

import com.hust.baseweb.entity.UserLogin;

public interface UserService {
    UserLogin findById(String userLoginId);
}
