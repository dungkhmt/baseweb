package com.hust.baseweb.applications.humanresource.service;

import org.springframework.stereotype.Service;

import com.hust.baseweb.applications.humanresource.entity.Department;

@Service
public interface DepartmentService {
	Department save(String departmentName);
}
