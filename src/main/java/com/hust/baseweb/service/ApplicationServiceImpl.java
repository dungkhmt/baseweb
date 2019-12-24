package com.hust.baseweb.service;

import com.hust.baseweb.entity.Application;
import com.hust.baseweb.entity.SecurityPermission;
import com.hust.baseweb.repo.ApplicationRepo;
import com.hust.baseweb.repo.ApplicationTypeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ApplicationServiceImpl implements  ApplicationService {
    @Autowired
    private ApplicationRepo applicationRepo;
    @Autowired
    private ApplicationTypeRepo applicationTypeRepo;
    @Override
    public List<Application> getListByPermissionAndType(List<SecurityPermission> permissionList, String type) {
        List<Application> applicationList=applicationRepo.findByTypeAndPermissionIn(applicationTypeRepo.getOne(type),permissionList);
        List<Application> applicationList1=applicationList.stream().map(application -> application.getModule()).collect(Collectors.toList());
        List<Application> applicationList2=applicationList1.stream().map(application -> application.getModule()).collect(Collectors.toList());
        applicationList.addAll(applicationList1);
        applicationList.addAll(applicationList2);
        applicationList.removeIf(Objects::isNull);
        return applicationList;
    }
}
