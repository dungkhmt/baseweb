package com.hust.baseweb.service;

import com.hust.baseweb.entity.Application;
import com.hust.baseweb.entity.SecurityPermission;

import java.util.List;

public interface ApplicationService {
    public List<Application> getListByPermissionAndType(List<SecurityPermission> permissionList, String type);

}
