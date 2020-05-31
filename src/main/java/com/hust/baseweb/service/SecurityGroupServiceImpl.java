package com.hust.baseweb.service;

import com.hust.baseweb.entity.SecurityGroup;
import com.hust.baseweb.repo.SecurityGroupRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class SecurityGroupServiceImpl implements SecurityGroupService {

    SecurityGroupRepo securityGroupRepo;

    @Override
    public List<SecurityGroup> findAll() {
        return securityGroupRepo.findAll();
    }

}
