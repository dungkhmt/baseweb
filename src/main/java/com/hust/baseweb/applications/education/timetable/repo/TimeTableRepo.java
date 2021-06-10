package com.hust.baseweb.applications.education.timetable.repo;

import com.hust.baseweb.applications.education.timetable.entity.TimeTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TimeTableRepo extends JpaRepository<TimeTable, UUID> {

    TimeTable save(TimeTable timetable);
}
