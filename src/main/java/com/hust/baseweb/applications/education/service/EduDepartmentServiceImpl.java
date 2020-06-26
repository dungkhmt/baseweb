package com.hust.baseweb.applications.education.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hust.baseweb.applications.education.entity.EduDepartment;
import com.hust.baseweb.applications.education.repo.EduDepartmentRepo;

@Service
public class EduDepartmentServiceImpl implements EduDepartmentService {

	@Autowired
	EduDepartmentRepo departmentRepo;
	
	@Override
	public EduDepartment save(String departmentId, String departmentName) {
		// TODO Auto-generated method stub
		EduDepartment dept = departmentRepo.findByDepartmentId(departmentId);
		if (dept==null) {
			dept = new EduDepartment();
			dept.setDepartmentId(departmentId);
			dept.setDepartmentName(departmentName);
			departmentRepo.save(dept);
		}
		return dept;
	}

}
