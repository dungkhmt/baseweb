package com.hust.baseweb.applications.education.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hust.baseweb.applications.education.entity.EduDepartment;

@Service
public interface EduDepartmentService {
	EduDepartment save(String departmentId, String departmentName);
	List<EduDepartment> findAll();
}
