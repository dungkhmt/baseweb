package com.hust.baseweb.service;

import java.util.List;

import com.hust.baseweb.entity.Party;
import com.hust.baseweb.entity.UserLogin;
import com.hust.baseweb.model.PersonModel;

public interface UserService {
    public UserLogin findById(String userLoginId);
    public List<UserLogin> getAllUserLogins();
    public UserLogin save(String userName, String password) throws Exception;
    public Party save(PersonModel personModel,String createdBy) throws Exception;
}
