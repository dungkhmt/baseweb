package com.hust.baseweb.applications.humanresource.service;

import com.hust.baseweb.applications.humanresource.entity.PartyDepartment;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface PartyDepartmentService {
    PartyDepartment save(UUID partyId, String departmentId, String roleTypeId);
}
