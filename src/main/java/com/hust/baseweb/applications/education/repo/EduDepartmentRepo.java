package com.hust.baseweb.applications.education.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hust.baseweb.applications.education.entity.EduDepartment;

public interface EduDepartmentRepo extends JpaRepository<EduDepartment, String> {
	/*EduDepartment findByDepartmentId(String departmentId);*/
	EduDepartment save(EduDepartment department);

}
