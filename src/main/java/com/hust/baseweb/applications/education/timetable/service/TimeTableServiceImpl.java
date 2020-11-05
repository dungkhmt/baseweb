package com.hust.baseweb.applications.education.timetable.service;

import com.hust.baseweb.applications.education.timetable.entity.TimeTable;
import com.hust.baseweb.applications.education.timetable.model.TimeTableModelInput;
import com.hust.baseweb.applications.education.timetable.repo.TimeTableRepo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
public class TimeTableServiceImpl implements TimeTableService
{

    @Autowired
    private TimeTableRepo timetableRepo;

    @Override
    public TimeTable save(TimeTableModelInput input)
    {
        TimeTable timetable = new TimeTable();

        timetable.setCid(input.getCid());
        timetable.setAcid(input.getAcid());
        timetable.setCourse_code(input.getCourse_code());
        timetable.setCourse_name(input.getCourse_name());
        timetable.setCapacity(input.getCapacity());
        timetable.setNote(input.getNote());
        timetable.setCourse_week(input.getCourse_week());
        timetable.setDay_of_week(input.getDay_of_week());
        timetable.setCourse_time(input.getCourse_time());
        timetable.setStart_section(input.getStart_section());
        timetable.setFinish_section(input.getFinish_section());
        timetable.setShift(input.getShift());
        timetable.setRoom(input.getRoom());
        timetable.setType_of_class(input.getType_of_class());
        timetable.setTotal(input.getTotal());
        timetable.setCourse_session(input.getCourse_session());
        timetable.setState(input.getState());
        timetable.setExperiment(input.getExperiment());
        timetable.setMid(input.getMid());
        timetable.setCollege(input.getCollege());
        timetable.setCourse_period(input.getCourse_period());

        timetableRepo.save(timetable);

        return null;
    }

    @Override
    public List<TimeTable> findAll()
    {
        List<TimeTable> timetablelist = timetableRepo.findAll();
        return timetablelist;
    }
}
