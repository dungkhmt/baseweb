package com.hust.baseweb.applications.humanresource.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hust.baseweb.applications.humanresource.entity.Department;

@Service
public interface DepartmentService {
	Department save(String departmentName);
	List<Department> findAll();
}
