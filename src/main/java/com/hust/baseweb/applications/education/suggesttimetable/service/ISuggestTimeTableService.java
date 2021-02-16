package com.hust.baseweb.applications.education.suggesttimetable.service;

import com.hust.baseweb.applications.education.exception.SimpleResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ISuggestTimeTableService {

    SimpleResponse uploadTimetable(MultipartFile file) throws IOException;
}
