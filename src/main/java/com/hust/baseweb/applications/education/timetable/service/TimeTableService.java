package com.hust.baseweb.applications.education.timetable.service;

import com.hust.baseweb.applications.education.timetable.entity.TimeTable;
import com.hust.baseweb.applications.education.timetable.model.TimeTableModelInput;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TimeTableService
{
    TimeTable save(TimeTableModelInput input);
    List<TimeTable> findAll();
}
