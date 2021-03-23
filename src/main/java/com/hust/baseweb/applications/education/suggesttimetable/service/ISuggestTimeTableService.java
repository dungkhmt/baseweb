package com.hust.baseweb.applications.education.suggesttimetable.service;

import com.hust.baseweb.applications.education.exception.SimpleResponse;
import com.hust.baseweb.applications.education.suggesttimetable.model.TimetableOM;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface ISuggestTimeTableService {

    SimpleResponse uploadTimetable(MultipartFile file) throws IOException;

    List<TimetableOM[]> getAllTimetablesOfCourses(final Set<String> courseIds);
}
