package com.hust.baseweb.applications.education.suggesttimetable.service;

import com.hust.baseweb.applications.education.exception.SimpleResponse;
import com.hust.baseweb.applications.education.suggesttimetable.model.EduClassOM;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface ISuggestTimeTableService {

    SimpleResponse uploadTimetable(MultipartFile file) throws IOException;

    List<List<EduClassOM>> getAllTimetablesOfCourses(final Set<String> courseIds);
}
