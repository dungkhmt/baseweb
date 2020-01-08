package com.hust.baseweb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hust.baseweb.entity.SecurityGroup;
import com.hust.baseweb.repo.SecurityGroupRepo;

@Service
public class SecurityGroupServiceImpl implements SecurityGroupService {

	@Autowired
	SecurityGroupRepo securityGroupRepo;
	
	@Override
	public List<SecurityGroup> findAll() {
		// TODO Auto-generated method stub
		return securityGroupRepo.findAll();
	}

}
