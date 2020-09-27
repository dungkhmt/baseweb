package com.hust.baseweb.applications.education.classmanagement.service;

import org.springframework.http.ResponseEntity;

public interface ClassService {

    ResponseEntity<?> getClassListOfCurrentSemester(int page, int size);
}
