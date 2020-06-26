package com.hust.baseweb.applications.humanresource.repo;

import com.hust.baseweb.applications.humanresource.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepo extends JpaRepository<Department, String> {

    Department save(Department dept);

    Department findByDepartmentId(String departmentId);
}
