package com.hust.baseweb.service;

import java.util.List;
import java.util.UUID;

import com.hust.baseweb.entity.Party;
import com.hust.baseweb.entity.UserLogin;
import com.hust.baseweb.repo.UserLoginRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements  UserService {
	public static final String module = UserService.class.getName();
    @Autowired
    private UserLoginRepo userLoginRepo;
    @Autowired
    PartyService partyService;
    
   
    
    @Override
    public UserLogin findById(String userLoginId) {
        return userLoginRepo.findByUserLoginId(userLoginId);
    }
    public List<UserLogin> getAllUserLogins(){
    	return userLoginRepo.findAll();
    }
    @Override
    @Transactional
    public UserLogin save(String userName, String password) throws Exception{
    	Party p = partyService.save("PERSON");
    	UserLogin ul = new UserLogin(userName, password, null, true);
    	ul.setParty(p);
    	if(userLoginRepo.existsById(userName)){
    		System.out.println(module + "::save, userName " + userName + " EXISTS!!!");
    		throw new RuntimeException();
    	}
    	return userLoginRepo.save(ul);
    }
}
