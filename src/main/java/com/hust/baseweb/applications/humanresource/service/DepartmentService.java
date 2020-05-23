package com.hust.baseweb.applications.humanresource.service;

import com.hust.baseweb.applications.humanresource.entity.Department;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DepartmentService {
    Department save(String departmentName);

    List<Department> findAll();
}
