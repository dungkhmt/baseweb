package com.hust.baseweb.applications.education.suggesttimetable.controller;

import com.hust.baseweb.applications.education.exception.SimpleResponse;
import com.hust.baseweb.applications.education.suggesttimetable.service.ISuggestTimeTableService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/suggest-timetable")
public class SuggestTimetableController {

    private final ISuggestTimeTableService timeTableService;

    /**
     * Return all feasible timetable of courses in {@code courseIds}.
     *
     * @param courseIds
     * @return
     */
    @GetMapping
    public ResponseEntity<?> getTimetables(@RequestParam Set<String> courseIds) {
        return ResponseEntity.ok().body(timeTableService.getAllTimetablesOfCourses(courseIds));
    }

    /**
     * @param file
     * @return
     */
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
