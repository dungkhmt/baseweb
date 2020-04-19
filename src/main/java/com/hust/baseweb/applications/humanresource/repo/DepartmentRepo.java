package com.hust.baseweb.applications.humanresource.repo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hust.baseweb.applications.humanresource.entity.Department;

public interface DepartmentRepo extends JpaRepository<Department, String> {
	Department save(Department dept);
	Department findByDepartmentId(String departmentId);
}
