package com.hust.baseweb.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.hust.baseweb.entity.Party;
import com.hust.baseweb.entity.UserLogin;
import com.hust.baseweb.model.PersonModel;
import com.hust.baseweb.rest.user.DPerson;

public interface UserService {
    public UserLogin findById(String userLoginId);
    public Page<DPerson> findAllPerson(Pageable page);
    public List<UserLogin> getAllUserLogins();
    public UserLogin save(String userName, String password) throws Exception;
    public Party save(PersonModel personModel,String createdBy) throws Exception;
}
