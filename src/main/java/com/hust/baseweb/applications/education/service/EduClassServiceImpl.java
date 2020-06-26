package com.hust.baseweb.applications.education.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hust.baseweb.applications.education.entity.EduClass;
import com.hust.baseweb.applications.education.repo.EduClassRepo;

@Service
public class EduClassServiceImpl implements EduClassService {
	
	@Autowired
	EduClassRepo classRepo;

	@Override
	public EduClass save(String classId, String className, String classType, String courseId, String sessionId,
			String departmentId, String semesterId) {
		// TODO Auto-generated method stub
		EduClass clas = classRepo.findByClassId(classId);
		
		if (clas==null) {
			clas = new EduClass();
			clas.setClassId(classId);
			clas.setClassName(className);
			clas.setClassType(classType);
			clas.setCourseId(courseId);
			clas.setSessionId(sessionId);
			clas.setDepartmentId(departmentId);
			clas.setSemesterId(semesterId);
			classRepo.save(clas);
		}
		return clas;
	}

	@Override
	public List<EduClass> findAll() {
		// TODO Auto-generated method stub
		return classRepo.findAll();
	}

	@Override
	public List<EduClass> findBySemesterId(String semesterId) {
		// TODO Auto-generated method stub
		return classRepo.findBySemesterId(semesterId);
	}
}
