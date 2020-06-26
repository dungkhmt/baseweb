package com.hust.baseweb.applications.education.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hust.baseweb.applications.education.entity.EduClass;
import com.hust.baseweb.applications.education.model.ClassesInputModel;

@Service
public interface ClassesExcelService {
	List<EduClass> save(List<ClassesInputModel> input, String semesterId);
}
