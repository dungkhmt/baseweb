package com.hust.baseweb.applications.education.suggesttimetable.controller;

import com.hust.baseweb.applications.education.exception.SimpleResponse;
import com.hust.baseweb.applications.education.suggesttimetable.service.SuggestTimeTableServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;

@Controller
@RequestMapping("/suggest-timetable")
@AllArgsConstructor(onConstructor_ = @Autowired)
public class SuggestTimetableController {

    private final SuggestTimeTableServiceImpl timeTableService;

    @GetMapping
    public ResponseEntity<?> getAllClasses(@RequestParam Set<String> courseIds) {
        return ResponseEntity.ok().body(timeTableService.getAllClassesOfCourses(courseIds));
    }

    @PostMapping
    public ResponseEntity<?> uploadTimetable(@RequestParam("file") MultipartFile file) {
        SimpleResponse response = null;
        try {
            response = timeTableService.uploadTimetable(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
